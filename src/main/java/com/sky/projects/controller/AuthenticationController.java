package com.sky.projects.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sky.projects.model.dto.AuthRequestDTO;
import com.sky.projects.model.dto.AuthResponseDTO;

@RestController
@RequestMapping("/v1/auth")
public class AuthenticationController {

    @Autowired
    private com.sky.projects.service.AuthenticationService AuthenticationService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequestDTO user) {
        try {
            String token = AuthenticationService.generateToken(user);
            return ResponseEntity.ok(new AuthResponseDTO(token));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }
}