package com.software.inq.controller.IntegrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.software.inq.model.Event;
import com.software.inq.model.Ticket;
import com.software.inq.model.TicketStatus;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.hasSize;
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

    //Happy-path tests
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

    @Test
    void shouldFetchTicketById() throws Exception{
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

        Ticket ticket = new Ticket();
        ticket.setEvent(event);
        ticket.setUser(user);
        ticket.setStatus(TicketStatus.VALID);
        ticket.setQrCode("qwertz");
        ticketRepository.save(ticket);

        mockMvc.perform(get("/api/tickets/" + ticket.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ticket.getId()))
                .andExpect(jsonPath("$.status").value("VALID"))
                .andExpect(jsonPath("$.qrCode").isNotEmpty())
                .andExpect(jsonPath("$.userId").value(user.getId()))
                .andExpect(jsonPath("$.eventId").value(event.getId()));
    }

    @Test
    void shouldReturnAllTickets() throws Exception{
        User alice = new User();
        alice.setName("Alice");
        alice.setAge(23);
        alice.setEmailAddress("alice@inq.com");
        alice.setTickets(Set.of());
        userRepository.save(alice);

        User bob = new User();
        bob.setName("Bob");
        bob.setAge(23);
        bob.setEmailAddress("bob@inq.com");
        bob.setTickets(Set.of());
        userRepository.save(bob);

        Event event = new Event();
        event.setName("Hackathon");
        event.setLocation("Saarburg");
        event.setDate(LocalDateTime.now());
        event.setTickets(Set.of());
        eventRepository.save(event);

        Ticket ticketAlice = new Ticket();
        ticketAlice.setEvent(event);
        ticketAlice.setUser(alice);
        ticketAlice.setStatus(TicketStatus.VALID);
        ticketAlice.setQrCode("qwertz");
        ticketRepository.save(ticketAlice);

        Ticket ticketBob = new Ticket();
        ticketBob.setEvent(event);
        ticketBob.setUser(bob);
        ticketBob.setStatus(TicketStatus.VALID);
        ticketBob.setQrCode("12345");
        ticketRepository.save(ticketBob);

        mockMvc.perform(get("/api/tickets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].status").value("VALID"))
                .andExpect(jsonPath("$[0].qrCode").value("qwertz"))
                .andExpect(jsonPath("$[0].userId").value(alice.getId()))
                .andExpect(jsonPath("$[0].eventId").value(event.getId()))

                .andExpect(jsonPath("$[1].id").exists())
                .andExpect(jsonPath("$[1].status").value("VALID"))
                .andExpect(jsonPath("$[1].qrCode").value("12345"))
                .andExpect(jsonPath("$[1].userId").value(bob.getId()))
                .andExpect(jsonPath("$[1].eventId").value(event.getId()));

        assertThat(ticketRepository.count()).isEqualTo(2);


    }

    @Test
    void shouldDeleteTicket() throws Exception{

        User alice = new User();
        alice.setName("Alice");
        alice.setAge(23);
        alice.setEmailAddress("alice@inq.com");
        alice.setTickets(Set.of());
        userRepository.save(alice);

        Event event = new Event();
        event.setName("Hackathon");
        event.setLocation("Saarburg");
        event.setDate(LocalDateTime.now());
        event.setTickets(Set.of());
        eventRepository.save(event);

        Ticket ticket = new Ticket();
        ticket.setEvent(event);
        ticket.setUser(alice);
        ticket.setStatus(TicketStatus.VALID);
        ticket.setQrCode("qwertz");
        ticketRepository.save(ticket);

        mockMvc.perform(delete("/api/tickets/" + ticket.getId()))
                .andExpect(status().isNoContent());

        assertThat(ticketRepository.count()).isZero();
        assertThat(ticketRepository.findById(ticket.getId())).isEmpty();
    }

    //TODO: shouldUpdateTicketStatus

    //Error-handling tests
    @Test
    void shouldReturn404WhenUserNotFound()throws Exception {

        Event event = new Event();
        event.setName("Hackathon");
        event.setLocation("Saarburg");
        event.setDate(LocalDateTime.now());
        event.setTickets(Set.of());
        eventRepository.save(event);

        String json = """
                {
                    "userId": 1,
                    "eventId": %d
                }
                """.formatted(event.getId());

        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User with id 1 not found."));

        assertThat(ticketRepository.count()).isZero();
    }

    @Test
    void shouldReturn404WhenEventNotFound() throws Exception{

        User user = new User();
        user.setName("Alice");
        user.setAge(23);
        user.setEmailAddress("alice@inq.com");
        user.setTickets(Set.of());
        userRepository.save(user);

        String json = """
                {
                    "userId": %d,
                    "eventId": 1
                }
                """.formatted(user.getId());

        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Event with id 1 not found."));

        assertThat(ticketRepository.count()).isZero();
    }

    @Test
    void shouldReturn400WhenInvalidPayload() throws Exception{

        String json = """
                {
                }
                """;

        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(2)))
                .andExpect(jsonPath("$.errors[0]").value("eventId is required"))
                .andExpect(jsonPath("$.errors[1]").value("userId is required"));

        assertThat(ticketRepository.count()).isZero();
    }

    //TODO: shouldReturn404WhenTicketNotFound
    //TODO: shouldNotAllowDuplicateTicketForSameUserAndEvent

    //Business-logic tests
    //TODO: shouldValidateTicket
    //TODO: shouldRejectAlreadyUserTicket --> 400/409




}
