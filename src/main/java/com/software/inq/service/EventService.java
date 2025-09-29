package com.software.inq.service;

import com.software.inq.dto.EventDTO;
import com.software.inq.mapper.EventMapper;
import com.software.inq.model.Event;
import com.software.inq.model.Ticket;
import com.software.inq.repository.EventRepository;
import com.software.inq.repository.TicketRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;

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

    public EventDTO create(EventDTO eventDTO){
        Event event = EventMapper.toEntity(eventDTO);

        Set<Long> ticketIds = eventDTO.ticketIds() != null ? eventDTO.ticketIds() : Set.of();

        Set<Ticket> tickets = ticketIds.stream()
                .map(id -> ticketRepository.findById(id)
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "Ticket with id " + id + " not found")))
                .collect(Collectors.toSet());

        event.setTickets(tickets);

        Event savedEvent = eventRepository.save(event);
        return EventMapper.toDTO(savedEvent);
    }

    public EventDTO update(Long id, EventDTO eventDTO){
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Event with id " + id + " not found"));

        event.setName(eventDTO.name());
        event.setLocation(eventDTO.location());
        event.setDate(eventDTO.date());

        if (eventDTO.ticketIds() != null && !eventDTO.ticketIds().isEmpty()) {
            Set<Ticket> tickets = eventDTO.ticketIds().stream()
                    .map(ticketId -> ticketRepository.findById(ticketId)
                            .orElseThrow(() -> new ResponseStatusException(
                                    HttpStatus.NOT_FOUND, "Ticket with id " + ticketId + " not found")))
                    .collect(Collectors.toSet());
            event.setTickets(tickets);
        }

        Event updatedEvent = eventRepository.save(event);
        return EventMapper.toDTO(updatedEvent);
    }

    public void delete(Long id){
        eventRepository.deleteById(id);
    }

    public EventDTO addTicket(Long eventId, Long ticketId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event with id " + eventId + " not found"));

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket with id " + ticketId + " not found"));

        event.getTickets().add(ticket);

        Event savedEvent = eventRepository.save(event);

        return EventMapper.toDTO(savedEvent);
    }
}
