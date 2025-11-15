package com.sky.projects.service;

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
import com.sky.projects.security.jwt.JwtService;

@RestController
@RequestMapping("/auth")
public class AuthenticationService {

    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequestDTO user) {
        try {
            String token = jwtService.generateToken(user.email());
            return ResponseEntity.ok(new AuthResponseDTO(token));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }
}