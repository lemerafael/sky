package com.sky.projects.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import com.sky.projects.service.ExternalProjectManagementService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.java.Log;

@RestController
@Log
@RequestMapping("v1/projects")
@Tag(name = "Project Management", description = "Project CRUD operations")
public class ExternalProjectController {
    @Autowired
    private ExternalProjectManagementService projectManagementService;

    @Autowired
    private ExternalProjectMapper projMapper;

    @PostMapping
    @Operation(summary = "Add new project to user by user ID", description = "Add new project to a specific user by their unique identifier")
    public ResponseEntity<?> addTo(Long userId, @RequestBody ExternalProjectDTO projectReq) {
        try {
            ExternalProject createdProject = projectManagementService.createExternalProject(userId, projectReq);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("")
                    .buildAndExpand(createdProject.getId())
                    .toUri();

            return ResponseEntity.created(location).body(projMapper.toDto(createdProject));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.severe("Error creating project: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    @Operation(summary = "Get projects from user by user ID", description = "Retrieve all projects from a specific user by their unique identifier")
    public ResponseEntity<List<ExternalProjectDTO>> get(Long userId) {
        try {
            List<ExternalProjectDTO> projectDTOs;
            projectDTOs = projMapper.toDtoList(projectManagementService.getAllProjectsByUserId(userId));
            return ResponseEntity.ok(projectDTOs);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.severe("Error retrieving user: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
