package com.software.inq.mapper;

import com.software.inq.dto.EventDTO;
import com.software.inq.model.Event;
import com.software.inq.model.Ticket;

import java.util.stream.Collectors;

public class EventMapper {

    private EventMapper(){

    }

    public static EventDTO toDTO(Event event){
        return EventDTO.builder()
                .id(event.getId())
                .name(event.getName())
                .ticketIds(event.getTickets().stream()
                        .map(Ticket::getId)
                        .collect(Collectors.toSet()))
                .location(event.getLocation())
                .date(event.getDate())
                .build();
    }

    /**
     * Tickets needs to be set in service class
     */
    public static Event toEntity(EventDTO dto){
        Event event = new Event();
        event.setId(dto.id());
        event.setName(dto.name());
        event.setLocation(dto.location());
        event.setDate(dto.date());
        return event;

    }
}
