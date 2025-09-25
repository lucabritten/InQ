package com.software.inq.controller;

import com.software.inq.dto.EventDTO;
import com.software.inq.service.EventService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@AllArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping
    public ResponseEntity<List<EventDTO>> getAll(){
       List<EventDTO> events = eventService.getAll();
       return ResponseEntity.ok(events);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getOne(@PathVariable Long id){
        EventDTO event = eventService.getOne(id);
        return ResponseEntity.ok(event);
    }

    @PostMapping
    public ResponseEntity<EventDTO> create(@RequestBody EventDTO eventDTO){
        EventDTO savedEvent = eventService.create(eventDTO);
        return ResponseEntity
                .created(URI.create("/api/events" + savedEvent.id()))
                .body(savedEvent);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventDTO> update(@PathVariable Long id, @RequestBody EventDTO eventDTO){
        EventDTO updatedEvent = eventService.update(id,eventDTO);
        return ResponseEntity.ok(updatedEvent);
    }
}
