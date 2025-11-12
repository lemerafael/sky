package com.sky.projects;

import java.util.List;
import java.util.logging.Level;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import com.sky.projects.model.persistence.User;
import com.sky.projects.repository.UserRepository;

import lombok.extern.java.Log;

@Log
@SpringBootApplication
public class DemoApplication {
    @Autowired 
    private UserRepository repository; 
  
    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() {
        List<User> allUsers = this.repository.findAll(); 
        log.log(Level.INFO, "Number of users: " + allUsers.size());
 
        User newUser = new User(); 
        newUser.setName("John"); 
        newUser.setPassword("pass"); 
        newUser.setEmail("email@email.com"); 
        log.log(Level.INFO, "Saving new user..."); 
        this.repository.save(newUser); 
 
        allUsers = this.repository.findAll(); 
        log.log(Level.INFO, "Number of users: " + allUsers.size());
    }
}