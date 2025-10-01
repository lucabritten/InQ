package com.software.inq.controller.MockitoTest;

import com.software.inq.controller.UserController;
import com.software.inq.dto.UserDTO;
import com.software.inq.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
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

    @Test
    void shouldReturnNotFoundWhenIdDoesNotExist() throws Exception{

        when(userService.getOne(99L)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        mockMvc.perform(get("/api/users/99")
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateNewUser() throws Exception{

        String userJson = """
                {
                    "name": "Bob",
                    "age": 21,
                    "emailAddress": "bob@inq.com",
                    "ticketIds": []
                }
                """;
        UserDTO savedUser = UserDTO.builder()
                .id(1L)
                .name("Bob")
                .age(21)
                .emailAddress("bob@inq.com")
                .ticketIds(Set.of())
                .build();

        when(userService.create(any(UserDTO.class)))
                .thenReturn(savedUser);

        mockMvc.perform(post("/api/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(userJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Bob"))
                .andExpect(jsonPath("$.age").value("21"))
                .andExpect(jsonPath("$.emailAddress").value("bob@inq.com"));
    }

    @Test
    void shouldUpdateExistingUser() throws Exception{
        String updateJson = """
                {
                    "name": "Bob",
                    "age": 21,
                    "emailAddress": "bob@inq.com",
                    "ticketIds": []
                }
                """;

        UserDTO updatedUser = UserDTO.builder()
                .id(1L)
                .name("Bob")
                .age(21)
                .emailAddress("bob@inq.com")
                .ticketIds(Set.of())
                .build();

        when(userService.update(any(Long.class), any(UserDTO.class)))
                .thenReturn(updatedUser);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Bob"))
                .andExpect(jsonPath("$.age").value("21"))
                .andExpect(jsonPath("$.emailAddress").value("bob@inq.com"));
    }

    @Test
    void shouldDeleteUserWhenIdExists() throws Exception{
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistentUser() throws Exception{
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"))
                .when(userService).delete(99L);

        mockMvc.perform(delete("/api/users/99"))
                .andExpect(status().isNotFound());
    }
}
