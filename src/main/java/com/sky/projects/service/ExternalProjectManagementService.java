package com.sky.projects.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sky.projects.model.dto.ExternalProjectDTO;
import com.sky.projects.model.persistence.ExternalProject;
import com.sky.projects.model.persistence.User;
import com.sky.projects.repository.ExternalProjectRepository;

@Service
@Transactional
public class ExternalProjectManagementService {

    @Autowired
    private ExternalProjectRepository projectRepository;

    @Autowired
    private UserManagementService userManagementService;

    /**
     * Create a new external project for user
     * 
     * @param userId User identifier
     * @param projectDTO Project data transfer object
     * @return Created external project entity
     * @throws IllegalArgumentException if project already exists
     * or if user does not exist
     */
    public ExternalProject createExternalProject(long userId, ExternalProjectDTO projectDTO) {
        if (projectRepository.findByUser_IdAndName(userId, projectDTO.name()).isPresent()) {
            throw new IllegalArgumentException("Project already exists");
        }

        Optional<User> userOptional = userManagementService.getUserById(userId);
        User user;
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User does not exist");
        } else {
            user = userOptional.get();
        }

        ExternalProject externalProject = new ExternalProject();
        externalProject.setName(projectDTO.name());
        externalProject.setUser(user);
        return projectRepository.save(externalProject);
    }

    /**
     * Retrieve projects by user ID
     * 
     * @param userId User identifier
     * @return List of projects by user ID
     */
    public List<ExternalProject> getAllProjectsByUserId(Long userId) {
        return projectRepository.findAllByUser_Id(userId);
    }
    
}
