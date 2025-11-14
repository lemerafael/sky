package com.sky.projects.service;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.sky.projects.model.dto.UserDTO;
import com.sky.projects.model.dto.UserMapper;
import com.sky.projects.model.persistence.User;
import com.sky.projects.repository.UserRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.java.Log;

@RestController
@Log
@Tag(name = "User", description = "Controller for Users")
public class UserService {
    @Autowired 
    private UserRepository repository; 

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/get")
    @Operation(summary = "Get All Users")
    public List<UserDTO> getAll() {
        List<User> users = repository.findAll();
        return userMapper.toDtoList(users);
    }

    @GetMapping("/get/{id}")
    @Operation(summary = "Get User by id")
    public UserDTO get(Long id) {
        Optional<User> userRepo = repository.findById(id);
        User user = null;
        if(userRepo.isPresent()) {
            user = userRepo.get();
        }
        return userMapper.toDto(user);
    }

    @PostMapping("/new")
    @Operation(summary = "Create New User")
    public ResponseEntity<?> create(@RequestBody UserDTO customerReq) {
        log.log(Level.SEVERE, "Email: " + customerReq.email());
        log.log(Level.SEVERE, "Password: " + customerReq.password());
        log.log(Level.SEVERE, "Name: " + customerReq.name());
        User user = new User();
        user.setEmail(customerReq.email());
        user.setPassword(customerReq.password());
        user.setName(customerReq.name());
        User saved = repository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();
        return ResponseEntity.created(location).body(saved);
    }
}

