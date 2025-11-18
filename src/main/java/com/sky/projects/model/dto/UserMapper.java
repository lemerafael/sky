package com.sky.projects.model.dto;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.sky.projects.model.persistence.User;

@Component
public class UserMapper {

    public UserDTO toDto(User entity) {
        if (entity == null) {
            return null;
        }
        return new UserDTO(entity.getEmail(), entity.getPassword(), entity.getName());
    }
    
    public UserResponseDTO toResponseDto(User entity) {
        if (entity == null) {
            return null;
        }
        return new UserResponseDTO(entity.getId(), entity.getEmail(), entity.getName());
    }

    public List<UserResponseDTO> toResponseDtoList(List<User> entities) {
        return entities.stream()
            .map(this::toResponseDto)
            .collect(Collectors.toList());
    }
}
