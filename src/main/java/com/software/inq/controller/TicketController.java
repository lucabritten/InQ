package com.software.inq.controller;

import com.software.inq.dto.TicketDTO;
import com.software.inq.service.TicketService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@AllArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @GetMapping
    public ResponseEntity<List<TicketDTO>> getAll(){
        List<TicketDTO> tickets = ticketService.getAll();
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("{id}")
    public ResponseEntity<TicketDTO> getOne(@PathVariable Long id){
        TicketDTO ticket = ticketService.getOne(id);
        return ResponseEntity.ok(ticket);
    }

    @PostMapping
    public ResponseEntity<TicketDTO> create(@RequestBody TicketDTO ticketDTO){
        System.out.println("POST received: " + ticketDTO);
        TicketDTO savedTicket = ticketService.create(ticketDTO);
        return ResponseEntity
                .created(URI.create("/api/tickets/" + savedTicket.id()))
                .body(savedTicket);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TicketDTO> update(@PathVariable Long id, @RequestBody TicketDTO ticketDTO){
        TicketDTO updatedTicket = ticketService.update(id, ticketDTO);
        return ResponseEntity.ok(updatedTicket);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        ticketService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
