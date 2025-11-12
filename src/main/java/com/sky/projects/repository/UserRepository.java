package com.sky.projects.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sky.projects.dto.User;

public interface UserRepository extends JpaRepository<User, Long> { }