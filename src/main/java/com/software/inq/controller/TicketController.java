package com.software.inq.controller;

import com.software.inq.dto.TicketDTO;
import com.software.inq.service.EventService;
import com.software.inq.service.UserService;
import com.software.inq.util.PdfUtil;
import com.software.inq.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@AllArgsConstructor
@Tag(name = "Tickets", description = "Ticket Management API")
public class TicketController {

    private final TicketService ticketService;

    @GetMapping
    @Operation(summary = "get all Tickets")
    public ResponseEntity<List<TicketDTO>> getAll(){
        List<TicketDTO> tickets = ticketService.getAll();
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("{id}")
    @Operation(summary = "get a specific Ticket by id")
    public ResponseEntity<TicketDTO> getOne(@PathVariable Long id){
        TicketDTO ticket = ticketService.getOne(id);
        return ResponseEntity.ok(ticket);
    }

    @PostMapping
    @Operation(summary = "creates a new Ticket")
    public ResponseEntity<TicketDTO> create(@RequestBody TicketDTO ticketDTO){
        System.out.println("POST received: " + ticketDTO);
        TicketDTO savedTicket = ticketService.create(ticketDTO);
        return ResponseEntity
                .created(URI.create("/api/tickets/" + savedTicket.id()))
                .body(savedTicket);
    }

    @PutMapping("/{id}")
    @Operation(summary = "updates a specific Ticket by id")
    public ResponseEntity<TicketDTO> update(@PathVariable Long id, @RequestBody TicketDTO ticketDTO){
        TicketDTO updatedTicket = ticketService.update(id, ticketDTO);
        return ResponseEntity.ok(updatedTicket);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "deletes a specific Ticket by id")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        ticketService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/qrcode")
    @Operation(summary = "get a QR-Code for a specific Ticket by id")
    public ResponseEntity<byte[]> getQrCode(@PathVariable Long id) {
        TicketDTO ticket = ticketService.getOne(id);
        byte[] qrImage = Base64.getDecoder().decode(ticket.qrCode());
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(qrImage);
    }

    @GetMapping("/{id}/pdf")
    @Operation(summary = "get a pdf-Ticket by id")
    public ResponseEntity<byte[]> getTicketPdf(@PathVariable Long id) {
        byte[] pdfBytes = ticketService.generateTicketPdf(id);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=ticket-" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}
