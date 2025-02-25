package com.example.itsupportticket.controller;

import com.example.itsupportticket.entity.User;
import com.example.itsupportticket.enums.Role;
import com.example.itsupportticket.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserController userController;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        user1 = new User(1L, "bob", Role.IT_SUPPORT);
        user2 = new User(2L, "ali", Role.EMPLOYEE);
    }

    @Test
    void testGetAllUsers() throws Exception {
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].username").value("bob"))
                .andExpect(jsonPath("$[1].username").value("ali"));

        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetUserById() throws Exception {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("bob"));

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testCreateUser() throws Exception {
        when(userRepository.save(any(User.class))).thenReturn(user1);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"mehdi\", \"role\": \"IT_SUPPORT\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("bob"));

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testDeleteUser() throws Exception {
        doNothing().when(userRepository).deleteById(1L);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk());

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testUpdateUser() throws Exception {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(userRepository.save(any(User.class))).thenReturn(user1);

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"bobUpdated\", \"role\": \"IT_SUPPORT\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("bobUpdated"));

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any(User.class));
    }
}
