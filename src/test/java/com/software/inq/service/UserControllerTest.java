package com.software.inq.service;

import com.software.inq.controller.UserController;
import com.software.inq.dto.UserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void shouldReturnAllUsers() throws Exception{
        UserDTO user = UserDTO.builder()
                .id(1L)
                .name("Bob")
                .age(21)
                .emailAddress("bob@inq.com")
                .ticketIds(Set.of())
                .build();

        when(userService.getAll()).thenReturn(List.of(user));

        mockMvc.perform(get("/api/users")
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Bob"))
                .andExpect(jsonPath("$[0].age").value("21"))
                .andExpect(jsonPath("$[0].emailAddress").value("bob@inq.com"));
    }

    @Test
    void shouldReturnUserWhenIdExists() throws Exception{
        UserDTO user = UserDTO.builder()
                .id(1L)
                .name("Bob")
                .age(21)
                .emailAddress("bob@inq.com")
                .ticketIds(Set.of())
                .build();

        when(userService.getOne(1L)).thenReturn(user);

        mockMvc.perform(get("/api/users/1")
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Bob"))
                .andExpect(jsonPath("$.age").value("21"))
                .andExpect(jsonPath("$.emailAddress").value("bob@inq.com"));
    }
}
