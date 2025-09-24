package com.software.inq.controller;

import com.software.inq.dto.EventDTO;
import com.software.inq.service.EventService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
