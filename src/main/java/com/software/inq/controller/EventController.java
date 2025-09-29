package com.software.inq.controller;

import com.software.inq.dto.EventDTO;
import com.software.inq.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@AllArgsConstructor
@Tag(name= "Events", description = "Event Management API")
public class EventController {

    private final EventService eventService;

    @Operation(summary = "get all Events", description = "returns all existing Events")
    @GetMapping
    public ResponseEntity<List<EventDTO>> getAll(){
       List<EventDTO> events = eventService.getAll();
       return ResponseEntity.ok(events);
    }

    @Operation(summary = "get one Event by id")
    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getOne(@PathVariable Long id){
        EventDTO event = eventService.getOne(id);
        return ResponseEntity.ok(event);
    }

    @Operation(summary = "create a new Event")
    @PostMapping
    public ResponseEntity<EventDTO> create(@RequestBody EventDTO eventDTO){
        EventDTO savedEvent = eventService.create(eventDTO);
        return ResponseEntity
                .created(URI.create("/api/events/" + savedEvent.id()))
                .body(savedEvent);
    }

    @Operation(summary = "update an Event by id")
    @PutMapping("/{id}")
    public ResponseEntity<EventDTO> update(@PathVariable Long id, @RequestBody EventDTO eventDTO){
        EventDTO updatedEvent = eventService.update(id,eventDTO);
        return ResponseEntity.ok(updatedEvent);
    }

    @Operation(summary = "delete one Event by id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        eventService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "add Ticket to Event by id")
    @PutMapping("/{eventId}/tickets/{ticketId}")
    public ResponseEntity<EventDTO> addTicket(@PathVariable Long eventId, @PathVariable Long ticketId) {
        EventDTO updatedEvent = eventService.addTicket(eventId, ticketId);
        return ResponseEntity.ok(updatedEvent);
    }
}
