package com.software.inq.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.software.inq.dto.EventDTO;
import com.software.inq.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class EventControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // wandelt DTOs in JSON um

    @Autowired
    private EventRepository eventRepository;

    @BeforeEach
    void cleanDb() {
        eventRepository.deleteAll(); // leere DB vor jedem Test
    }

    @Test
    void shouldCreateAndFetchEvent() throws Exception {
        // Arrange: baue DTO
        EventDTO eventDTO = EventDTO.builder()
                .name("Hackathon 2025")
                .location("Berlin")
                .date(LocalDateTime.of(2025, 11, 15, 9, 0))
                .build();

        String json = objectMapper.writeValueAsString(eventDTO);

        // Act: POST → Event erstellen
        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Hackathon 2025"))
                .andExpect(jsonPath("$.location").value("Berlin"));

        // Assert: GET → Event wieder abfragen
        mockMvc.perform(get("/api/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Hackathon 2025"))
                .andExpect(jsonPath("$[0].location").value("Berlin"));
    }
}