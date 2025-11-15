package com.sky.projects.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sky.projects.model.dto.UserDTO;
import com.sky.projects.model.persistence.User;
import com.sky.projects.repository.UserRepository;

import lombok.extern.java.Log;
import java.util.List;
import java.util.Optional;

@Service
@Log
@Transactional
public class UserManagementService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Create a new user with encoded password
     * 
     * @param userDTO User data transfer object
     * @return Created user entity
     * @throws IllegalArgumentException if email already exists
     */
    public User createUser(UserDTO userDTO) {
        if (userRepository.findByEmail(userDTO.email()).isPresent()) {
            throw new IllegalArgumentException("Email already registered");
        }

        User user = new User();
        user.setEmail(userDTO.email());
        user.setPassword(passwordEncoder.encode(userDTO.password()));
        user.setName(userDTO.name());

        log.info("Creating new user with email: " + userDTO.email());
        return userRepository.save(user);
    }

    /**
     * Retrieve user by ID
     * 
     * @param id User identifier
     * @return User if found
     */
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Retrieve all users
     * 
     * @return List of all users
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Update existing user
     * 
     * @param id      User identifier
     * @param userDTO Updated user data
     * @return Updated user entity
     * @throws IllegalArgumentException if user not found
     */
    public User updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));

        user.setEmail(userDTO.email());
        user.setPassword(passwordEncoder.encode(userDTO.password()));
        user.setName(userDTO.name());

        log.info("Updating user with id: " + id);
        return userRepository.save(user);
    }

    /**
     * Delete user by ID
     * 
     * @param id User identifier
     * @throws IllegalArgumentException if user not found
     */
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found with id: " + id);
        }
        log.info("Deleting user with id: " + id);
        userRepository.deleteById(id);
    }
}
