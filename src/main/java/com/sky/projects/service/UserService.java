package com.sky.projects.service;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.logging.Level;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
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
@RequestMapping("/users/v1")
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
    public ResponseEntity<UserDTO> get(@PathVariable("id") Long id) {
        try {
            Optional<User> userRepo = repository.findById(id);
            User user = userRepo.get();
            return ResponseEntity.ok().body(userMapper.toDto(user));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (MethodArgumentTypeMismatchException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/new")
    @Operation(summary = "Create New User")
    public ResponseEntity<?> create(@RequestBody UserDTO userReq) {
        log.log(Level.SEVERE, "Email: " + userReq.email());
        log.log(Level.SEVERE, "Password: " + userReq.password());
        log.log(Level.SEVERE, "Name: " + userReq.name());
        User user = new User();
        user.setEmail(userReq.email());
        user.setPassword(userReq.password());
        user.setName(userReq.name());
        User saved = repository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();
        return ResponseEntity.created(location).body(saved);
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Update User by id")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody UserDTO userReq) {
        Optional<User> userRepo = repository.findById(id);
        User updatedUser = null;
        if (userRepo.isPresent()) {
            User user = userRepo.get();
            user.setEmail(userReq.email());
            user.setPassword(userReq.password());
            user.setName(userReq.name());
            updatedUser = repository.save(user);
        }
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(updatedUser.getId())
                .toUri();
        return ResponseEntity.created(location).body(updatedUser);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user by id")
    public void delete(@PathVariable("id") Long id) {
        Optional<User> userRepo = repository.findById(id);
        if (userRepo.isPresent()) {
            repository.deleteById(id);
        }
    }
}

