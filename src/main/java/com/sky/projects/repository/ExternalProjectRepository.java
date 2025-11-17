package com.sky.projects.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sky.projects.model.persistence.ExternalProject;

public interface ExternalProjectRepository extends JpaRepository<ExternalProject, Long> {
    List<ExternalProject> findAllByUser_Id(Long userId);
    Optional<ExternalProject> findByUser_IdAndName(Long userId, String name);
 }