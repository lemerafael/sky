package com.sky.projects.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import com.sky.projects.model.dto.AuthRequestDTO;
import com.sky.projects.model.persistence.User;
import com.sky.projects.security.jwt.JwtService;

@Service
public class AuthenticationService {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserManagementService userService;

    public String generateToken(AuthRequestDTO authRequest) {
        Optional<User> userOptional = userService.getUserByEmail(authRequest.email());
        User user;
        if (!userOptional.isPresent()) {
            throw new BadCredentialsException("User not found");
        } else {
            user = userOptional.get();
            if (!userService.matchPassword(authRequest.password(), user)) {
                throw new BadCredentialsException("Invalid password");
            }
        }
        return jwtService.generateToken(user.getEmail());
    }
}
