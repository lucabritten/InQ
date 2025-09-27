package com.software.inq.controller;

import com.software.inq.dto.TicketDTO;
import com.software.inq.service.TicketService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<TicketDTO> getOne(){
        return null;
    }

    @PostMapping
    public ResponseEntity<TicketDTO> create(){
        return null;
    }

    @PutMapping
    public ResponseEntity<TicketDTO> update(){
        return null;
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(){
        return null;
    }
}
