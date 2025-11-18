package com.sky.projects;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.sky.projects.controller.ExternalProjectController;
import com.sky.projects.model.dto.ExternalProjectMapper;
import com.sky.projects.model.dto.UserDTO;
import com.sky.projects.model.dto.UserMapper;
import com.sky.projects.model.persistence.ExternalProject;
import com.sky.projects.model.persistence.User;
import com.sky.projects.service.ExternalProjectManagementService;
import com.sky.projects.service.UserManagementService;

@WebMvcTest(ExternalProjectController.class)
@Import({ ExternalProjectMapper.class, UserMapper.class })
@AutoConfigureMockMvc(addFilters = false)
public class ProjectServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserMapper userMapper;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private UserManagementService userService;

    @MockitoBean
    private ExternalProjectManagementService projectService;

    private final String projectsServicePrefix = "/v1/projects";

    @Test
    public void testGetProjects() throws Exception {
        ExternalProject project = mockDbProject("Test project");

        mockMvc.perform(get(projectsServicePrefix)
                .param("userId", String.valueOf(project.getUser().getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());
        verify(projectService).getAllProjectsByUserId(project.getUser().getId());
    }

    @Test
    public void testGetEmpty() throws Exception {
        User user = mockDbUser(1L);

        mockMvc.perform(
            get(projectsServicePrefix)
                .param("userId", String.valueOf(user.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
        verify(projectService).getAllProjectsByUserId(user.getId());
    }

    @Test
    public void testGetFromInvalidUser() throws Exception {
        /* TODO: correct
        mockDbUser(1L);

        mockMvc.perform(
            get(projectsServicePrefix)
                .param("userId", String.valueOf(Long.MAX_VALUE)))
                .andExpect(status().isNotFound());
        verify(projectService).getAllProjectsByUserId(Long.MAX_VALUE);
        */
    }

    @Test
    public void testGetFromInvalidEntry() throws Exception {
        mockDbUser(1L);

        mockMvc.perform(
            get(projectsServicePrefix)
                .param("userId", "none"))
                .andExpect(status().isBadRequest());
    }

    private ExternalProject mockDbProject(String name) {
        User user = mockDbUser(1L);
        ExternalProject externalProject = new ExternalProject();
        externalProject.setId(1L);
        externalProject.setName(name);
        externalProject.setUser(user);
        List<ExternalProject> projects = new ArrayList<>();
        projects.add(externalProject);
        when(projectService.getAllProjectsByUserId(1L)).thenReturn(projects);
        return externalProject;
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
