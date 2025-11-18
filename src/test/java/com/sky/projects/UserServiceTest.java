package com.sky.projects;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sky.projects.controller.UserController;
import com.sky.projects.model.dto.UserDTO;
import com.sky.projects.model.dto.UserMapper;
import com.sky.projects.model.dto.UserResponseDTO;
import com.sky.projects.model.persistence.User;
import com.sky.projects.service.UserManagementService;

@WebMvcTest(UserController.class)
@Import(UserMapper.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserMapper userMapper;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private UserManagementService userService;

    private final String usersServicePrefix = "/v1/users";

    @Test
    public void testGetUsers() throws Exception {
        Map<Long, User> users = mockDbUsers(1L, 2L, 3L);
        assertTrue(users.size() == 3);

        mockMvc.perform(get(usersServicePrefix))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());
        verify(userService).getAllUsers();
    }

    @Test
    public void testGetUsersEmpty() throws Exception {
        mockMvc.perform(get(usersServicePrefix))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
        verify(userService).getAllUsers();
    }

    @Test
    public void testGetUser() throws Exception {
        long searchedId = 1L;
        User user = mockDbUser(searchedId);

        mockMvc.perform(get(usersServicePrefix + "/" + searchedId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.email").value(user.getEmail()));

        verify(userService).getUserById(searchedId);
    }

    @Test
    public void testGetInvalidUser() throws Exception {
        mockDbUser(1L);

        long searchedId = Long.MAX_VALUE;
        mockMvc.perform(get(usersServicePrefix + "/" + searchedId))
                .andExpect(status().isNotFound());

        verify(userService).getUserById(searchedId);
    }

    @Test
    public void testGetInvalidSearch() throws Exception {
        mockDbUser(1L);

        String searchedId = "null";
        mockMvc.perform(get(usersServicePrefix + "/" + searchedId))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateUser() throws Exception {
        long searchedId = 1L;
        User user = mockDbUser(searchedId);
        UserDTO userDTO = userMapper.toDto(user);
        String userDTOJson = objectMapper.writeValueAsString(userDTO);
        mockMvc.perform(post(usersServicePrefix)
                .content(userDTOJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.email").value(user.getEmail()));

        verify(userService).createUser(checkSavedUserDTO(user));
    }

    private UserDTO checkSavedUserDTO(User input) {
        return argThat(savedUser -> savedUser != null &&
                savedUser.password().equals(input.getPassword()) &&
                savedUser.email().equals(input.getEmail()) &&
                savedUser.name().equals(input.getName()));
    }

    private User mockDbUser(long id) {
        User user = initUser(id);
        when(userService.getUserById(user.getId())).thenReturn(Optional.of(user));
        when(userService.createUser(checkSavedUserDTO(user))).thenReturn(user);
        when(passwordEncoder.encode(any(String.class))).thenReturn(user.getPassword());
        return user;
    }

    private Map<Long, User> mockDbUsers(long... ids) {
        Map<Long, User> users = new HashMap<>();
        for (long id : ids) {
            User user = mockDbUser(id);
            users.put(user.getId(), user);
        }
        when(userService.getAllUsers()).thenReturn(new ArrayList<>(users.values()));
        return users;
    }

    private User initUser(long id) {
        User user = new User();
        int idInt = (int) id;
        user.setId(id);
        String email = "user" + idInt + "@email.com";
        user.setEmail(email);
        String password = "pass" + idInt;
        user.setPassword(password);
        String name = "User" + idInt + " Name";
        user.setName(name);
        return user;
    }

}
