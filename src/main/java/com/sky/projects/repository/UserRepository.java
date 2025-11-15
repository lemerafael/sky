package com.sky.projects.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sky.projects.model.persistence.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}