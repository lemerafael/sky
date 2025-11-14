package com.sky.projects;

import java.util.List;
import java.util.logging.Level;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import com.sky.projects.model.persistence.ExternalProject;
import com.sky.projects.model.persistence.User;
import com.sky.projects.repository.ExternalProjectRepository;
import com.sky.projects.repository.UserRepository;

import lombok.extern.java.Log;

@Log
@SpringBootApplication
public class DemoApplication {
    @Autowired 
    private UserRepository userRepo;

    @Autowired
    private ExternalProjectRepository projectRepo;
  
    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() {
        List<User> allUsers = this.userRepo.findAll(); 
        log.log(Level.INFO, "Number of users: " + allUsers.size());
 
        User newUser = new User(); 
        newUser.setName("John"); 
        newUser.setPassword("pass"); 
        newUser.setEmail("email@email.com"); 
        log.log(Level.INFO, "Saving new user..."); 
        newUser = this.userRepo.save(newUser); 
 
        allUsers = this.userRepo.findAll(); 
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
}