package com.sky.projects;

import java.util.List;
import java.util.logging.Level;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import com.sky.projects.model.dto.UserDTO;
import com.sky.projects.model.persistence.ExternalProject;
import com.sky.projects.model.persistence.User;
import com.sky.projects.repository.ExternalProjectRepository;
import com.sky.projects.service.UserManagementService;

import lombok.extern.java.Log;

@Log
@SpringBootApplication
public class DemoApplication {
    @Autowired
    private UserManagementService userService;

    @Autowired
    private ExternalProjectRepository projectRepo;

    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() {
        List<User> allUsers = this.userService.getAllUsers();
        log.log(Level.INFO, "Number of users: " + allUsers.size());

        UserDTO newUserReq = new UserDTO("email@email.com", "pass", "John");
        log.log(Level.INFO, "Saving new user...");
        User newUser = this.userService.createUser(newUserReq);

        allUsers = this.userService.getAllUsers();
        log.log(Level.INFO, "Number of users: " + allUsers.size());

        List<ExternalProject> allProjects = this.projectRepo.findAll();
        log.log(Level.INFO, "Number of projects: " + allProjects.size());

        ExternalProject project = new ExternalProject();
        project.setId(1);
        project.setUser(newUser);
        project.setName("Project");
        log.log(Level.INFO, "Saving new project...");
        this.projectRepo.save(project);

        allProjects = this.projectRepo.findAll();
        log.log(Level.INFO, "Number of projects: " + allProjects.size());
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}