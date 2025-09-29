package com.software.inq.controller.IntegrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.software.inq.model.Event;
import com.software.inq.model.User;
import com.software.inq.repository.EventRepository;
import com.software.inq.repository.TicketRepository;
import com.software.inq.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import java.time.LocalDateTime;
import java.util.Set;

@SpringBootTest
@AutoConfigureMockMvc
public class TicketControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @BeforeEach
    void setUp(){
        ticketRepository.deleteAll();
        eventRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void shouldCreateAndFetchTicket() throws Exception{

        User user = new User();
        user.setName("Alice");
        user.setAge(23);
        user.setEmailAddress("alice@inq.com");
        user.setTickets(Set.of());
        userRepository.save(user);

        Event event = new Event();
        event.setName("Hackathon");
        event.setLocation("Saarburg");
        event.setDate(LocalDateTime.now());
        event.setTickets(Set.of());
        eventRepository.save(event);

        String json = """
                {
                    "userId": %d,
                    "eventId": %d
                }
                """.formatted(user.getId(),event.getId());

        mockMvc.perform(post("/api/tickets")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.status").value("VALID"))
                .andExpect(jsonPath("$.qrCode").isNotEmpty());

        mockMvc.perform(get("/api/tickets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].status").value("VALID"))
                .andExpect(jsonPath("$[0].qrCode").isNotEmpty())
                .andExpect(jsonPath("$[0].userId").value(user.getId()))
                .andExpect(jsonPath("$[0].eventId").value(event.getId()));
    }

    //+ Tests
    //TODO: shouldFetchTicketById
    //TODO: shouldReturnAllTickets
    //TODO: shouldUpdateTicketStatus
    //TODO: shouldDeleteTicket

    //- Tests
    //TODO: shouldReturn404WhenUserNotFound
    //TODO: shouldReturn404WhenEventNotFound
    //TODO: shouldReturn400WhenInvalidPayload
    //TODO: shouldReturn404WhenTicketNotFound
    //TODO: shouldNotAllowDuplicateTicketForSameUserAndEvent

    //Business logik Tests
    //TODO: shouldValidateTicket
    //TODO: shouldRejectAlreadyUserTicket --> 400/409




}
