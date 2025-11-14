package com.sky.projects.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sky.projects.model.persistence.ExternalProject;

public interface ExternalProjectRepository extends JpaRepository<ExternalProject, Long> { }