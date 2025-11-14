package com.sky.projects.model.dto;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.sky.projects.model.persistence.ExternalProject;

@Component
public class ExternalProjectMapper {
    
    public ExternalProjectDTO toDto(ExternalProject entity) {
        if (entity == null) {
            return null;
        }
        return new ExternalProjectDTO(entity.getName());
    }

    public List<ExternalProjectDTO> toDtoList(List<ExternalProject> entities) {
        return entities.stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }
}
