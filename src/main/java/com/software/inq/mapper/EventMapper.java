package com.software.inq.mapper;

import com.software.inq.dto.EventDTO;
import com.software.inq.model.Event;

public class EventMapper {

    public static EventDTO toDTO(Event event){
        return EventDTO.builder()
                .id(event.getId())
                .location(event.getLocation())
                .date(event.getDate())
                .build();
    }
}
