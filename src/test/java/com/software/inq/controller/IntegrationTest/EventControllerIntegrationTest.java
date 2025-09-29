package com.software.inq.controller.IntegrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.software.inq.dto.EventDTO;
import com.software.inq.model.Event;
import com.software.inq.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class EventControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EventRepository eventRepository;

    @BeforeEach
    void cleanDb() {
        eventRepository.deleteAll();
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

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Hackathon 2025"))
                .andExpect(jsonPath("$.location").value("Berlin"));

        mockMvc.perform(get("/api/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Hackathon 2025"))
                .andExpect(jsonPath("$[0].location").value("Berlin"));
    }

    @Test
    void shouldDeleteEvent() throws Exception {
        EventDTO eventDTO = EventDTO.builder()
                .name("To be deleted")
                .location("TestCity")
                .date(LocalDateTime.of(2025, 12, 1, 10, 0))
                .build();

        String json = objectMapper.writeValueAsString(eventDTO);

        String response = mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        EventDTO createdEvent = objectMapper.readValue(response, EventDTO.class);

        mockMvc.perform(delete("/api/events/" + createdEvent.id()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/events/" + createdEvent.id()))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdateEvent() throws Exception{
        Event event = new Event();
        event.setName("Regatta");
        event.setLocation("Koeln");
        event.setDate(LocalDateTime.now());
        event.setTickets(Set.of());

        EventDTO updatedEvent = EventDTO.builder()
                        .name("RacingLeague")
                        .location("Miami")
                        .build();

        String json = objectMapper.writeValueAsString(updatedEvent);
        eventRepository.save(event);

        mockMvc.perform(put("/api/events/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("RacingLeague"))
                .andExpect(jsonPath("$.location").value("Miami"));
    }
}