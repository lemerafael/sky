package com.sky.projects.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sky.projects.model.persistence.User;

public interface UserRepository extends JpaRepository<User, Long> { }