package com.software.inq.controller;

import com.software.inq.dto.TicketDTO;
import com.software.inq.model.Event;
import com.software.inq.model.Ticket;
import com.software.inq.model.TicketStatus;
import com.software.inq.repository.TicketRepository;
import com.software.inq.service.TicketService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(TicketController.class)
public class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TicketService ticketService;

    @Test
    void shouldReturnAllTickets() throws Exception{
        TicketDTO ticket = TicketDTO.builder()
                .id(1L)
                .eventId(11L)
                .userId(111L)
                .status(TicketStatus.VALID)
                .qrCode("qwertz123")
                .build();

        when(ticketService.getAll())
                .thenReturn(List.of(ticket));

        mockMvc.perform(get("/api/tickets")
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].eventId").value(11L))
                .andExpect(jsonPath("$[0].userId").value(111L))
                .andExpect(jsonPath("$[0].status").value("VALID"))
                .andExpect(jsonPath("$[0].qrCode").value("qwertz123"));

    }

    @Test
    void shouldReturnTicketWhenIdExists() throws Exception{
        TicketDTO ticket = TicketDTO.builder()
                .id(1L)
                .eventId(11L)
                .userId(111L)
                .status(TicketStatus.VALID)
                .qrCode("qwertz123")
                .build();

        when(ticketService.getOne(1L))
                .thenReturn(ticket);

        mockMvc.perform(get("/api/tickets/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpectAll(expectTicket(ticket));
    }

    @Test
    void shouldReturnNotFoundWhenIdDoesNotExist() throws Exception{
        when(ticketService.getOne(99L))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/api/tickets/99")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateNewTicket() throws Exception{
        String ticketJson = """
                {
                  "eventId": 1,
                  "userId": 1     
                }
                """;

        TicketDTO savedTicket = TicketDTO.builder()
                .id(1L)
                .eventId(1L)
                .userId(1L)
                .status(TicketStatus.VALID)
                .qrCode("qwertz123")
                .build();

        when(ticketService.create(any(TicketDTO.class)))
                .thenReturn(savedTicket);

        mockMvc.perform(post("/api/tickets")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(ticketJson))
                .andExpect(status().isCreated())
                .andExpectAll(expectTicket(savedTicket));
    }

    @Test
    void shouldUpdateExistingTicket() throws Exception{
        String ticketJson = """
                {
                    "status": "VALID",
                    "qeCode": "qwertz123"
                }
                """;

        TicketDTO updatedTicket = TicketDTO.builder()
                .id(1L)
                .eventId(11L)
                .userId(111L)
                .status(TicketStatus.VALID)
                .qrCode("qwertz123")
                .build();

        when(ticketService.update(any(Long.class), any(TicketDTO.class)))
                .thenReturn(updatedTicket);

        mockMvc.perform(put("/api/tickets/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ticketJson))
                .andExpect(status().isOk())
                .andExpectAll(expectTicket(updatedTicket));
    }

    @Test
    void shouldThrow404whenTryToUpdateNonExistentTicket() throws Exception{
        String ticketJson = """
                {
                    "status": "VALID",
                    "qeCode": "qwertz123"
                }
                """;

        when(ticketService.update(any(Long.class), any(TicketDTO.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mockMvc.perform(put("/api/tickets/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ticketJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteTicketWhenIdExists() throws Exception{
        mockMvc.perform(delete("/api/tickets/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistentTicket() throws Exception {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND))
                .when(ticketService).delete(99L);

        mockMvc.perform(delete("/api/events/99"))
                .andExpect(status().isNotFound());
    }

    private ResultMatcher[] expectTicket(TicketDTO ticket) {
        return new ResultMatcher[] {
                jsonPath("$.id").value(ticket.id()),
                jsonPath("$.eventId").value(ticket.eventId()),
                jsonPath("$.userId").value(ticket.userId()),
                jsonPath("$.status").value(ticket.status().toString()),
                jsonPath("$.qrCode").value(ticket.qrCode())
        };
    }
}
