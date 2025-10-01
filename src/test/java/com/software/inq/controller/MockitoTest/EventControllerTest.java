package com.software.inq.controller.MockitoTest;

import com.software.inq.controller.EventController;
import com.software.inq.dto.EventDTO;
import com.software.inq.service.EventService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EventController.class)
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventService eventService;

    @Test
    void shouldReturnAllEvents() throws Exception {
        EventDTO event = EventDTO.builder()
                .id(1L)
                .name("Hackathon 2025")
                .location("Berlin")
                .date(LocalDateTime.now().plusDays(1))
                .build();

        when(eventService.getAll()).thenReturn(List.of(event));

        mockMvc.perform(get("/api/events")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Hackathon 2025"))
                .andExpect(jsonPath("$[0].location").value("Berlin"));
    }

    @Test
    void shouldReturnEventWhenIdExists() throws Exception {
        EventDTO event = EventDTO.builder()
                .id(1L)
                .name("Hackathon 2025")
                .location("Berlin")
                .date(LocalDateTime.now().plusDays(1))
                .build();

        when(eventService.getOne(1L)).thenReturn(event);

        mockMvc.perform(get("/api/events/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Hackathon 2025"))
                .andExpect(jsonPath("$.location").value("Berlin"));
    }

    @Test
    void shouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        when(eventService.getOne(99L))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/api/events/99")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateNewEvent() throws Exception {
        String eventJson = """
                {
                    "name": "Rowing_World_Cup",
                    "location": "Lucerne",
                    "date": "2025-10-01T10:00:00"
                }
                """;
        EventDTO savedEvent = EventDTO.builder()
                .id(1L)
                .name("Rowing_World_Cup")
                .location("Lucerne")
                .date(LocalDateTime.of(2025, 10, 1, 10, 0))
                .build();

        when(eventService.create(any(EventDTO.class)))
                .thenReturn(savedEvent);

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(eventJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Rowing_World_Cup"))
                .andExpect(jsonPath("$.location").value("Lucerne"));
    }

    @Test
    void shouldUpdateExistingEvent() throws Exception{
        String updateJson = """
                {
                    "name": "Rowing_World_Cup",
                    "location": "Varese",
                    "date": "2025-10-01T10:00:00"
                }
                """;
        EventDTO updatedEvent = EventDTO.builder()
                .id(1L)
                .name("Rowing_World_Cup")
                .location("Varese")
                .date(LocalDateTime.of(2025, 12, 1, 12, 0))
                .build();

        when(eventService.update(any(Long.class), any(EventDTO.class)))
                .thenReturn(updatedEvent);

        mockMvc.perform(put("/api/events/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Rowing_World_Cup"))
                .andExpect(jsonPath("$.location").value("Varese"));
    }
    @Test
    void shouldThrow404whenTryToUpdateNonExistentEvent() throws Exception{
        String updateJson = """
            {
                "name": "Nonexistent Event",
                "location": "Nowhere",
                "date": "2025-10-01T10:00:00"
            }
            """;

        when(eventService.update(any(Long.class), any(EventDTO.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mockMvc.perform(put("/api/events/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteEventWhenIdExists() throws Exception {
        mockMvc.perform(delete("/api/events/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistentEvent() throws Exception {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND))
                .when(eventService).delete(99L);

        mockMvc.perform(delete("/api/events/99"))
                .andExpect(status().isNotFound());
    }
}
