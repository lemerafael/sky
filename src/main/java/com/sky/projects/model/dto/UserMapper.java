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

    public List<UserDTO> toDtoList(List<User> entities) {
        return entities.stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }
}
