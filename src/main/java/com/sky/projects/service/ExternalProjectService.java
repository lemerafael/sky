package com.sky.projects.service;

import java.net.URI;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.sky.projects.model.dto.ExternalProjectDTO;
import com.sky.projects.model.dto.ExternalProjectMapper;
import com.sky.projects.model.persistence.ExternalProject;
import com.sky.projects.model.persistence.User;
import com.sky.projects.repository.ExternalProjectRepository;
import com.sky.projects.repository.UserRepository;

import lombok.extern.java.Log;

@RestController
@Log
@RequestMapping("/projects/v1")
public class ExternalProjectService {
    @Autowired
    private ExternalProjectRepository projRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ExternalProjectMapper projMapper;

    @PostMapping("/add/{id}")
    public ResponseEntity<?> addTo(Long id, @RequestBody ExternalProjectDTO projectReq) {
        Optional<User> user = userRepo.findById(id);
        ResponseEntity response = null;
        if (user.isPresent()) {
            log.log(Level.SEVERE, "Found user: " + user.get().getName());
            List<ExternalProject> projects = projRepo.findAll();
            Optional<ExternalProject> max = projects.stream().max(Comparator.comparing(ExternalProject::getId));
            long maxId;
            if (max.isPresent()) {
                maxId = max.get().getId() + 1;
            } else {
                maxId = 1;
            }
            Optional<ExternalProject> tentative = projects.stream()
                                                        .filter(project -> project.getName().equals(projectReq.name()))
                                                        .findAny();
            ExternalProject externalProject = null;
            if (tentative.isPresent()) {
                externalProject = tentative.get();
            } else {
                externalProject = new ExternalProject();
            }
            externalProject.setName(projectReq.name());
            externalProject.setUser(user.get());
            externalProject.setId(maxId);
            projRepo.save(externalProject);
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(externalProject.getId())
                    .toUri();
            response = ResponseEntity.created(location).body(projMapper.toDto(externalProject));
        } else {
            log.log(Level.SEVERE, "User not found");
        }
        return response;
    }

    @GetMapping("/get/{id}")
    public List<ExternalProjectDTO> get(Long id) {
        List<ExternalProject> projects = projRepo.findAll().stream()
                                                            .filter(project -> project.getUser().getId() == id)
                                                            .toList();
        return projMapper.toDtoList(projects);
    }
    
}
