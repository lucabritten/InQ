package com.software.inq.service;

import com.software.inq.dto.EventDTO;
import com.software.inq.mapper.EventMapper;
import com.software.inq.repository.EventRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@AllArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public List<EventDTO> getAll(){
        return eventRepository.findAll().stream()
                .map(EventMapper::toDTO)
                .toList();
    }

    public EventDTO getOne(Long id){
        return eventRepository.findById(id)
                .map(EventMapper::toDTO)
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event with id " + id + " does not exist"));
    }
}
