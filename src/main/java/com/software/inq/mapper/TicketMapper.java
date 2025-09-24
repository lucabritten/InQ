package com.software.inq.mapper;

import com.software.inq.dto.TicketDTO;
import com.software.inq.model.Ticket;

public class TicketMapper {

    public static TicketDTO getDTO(Ticket ticket){
        return TicketDTO.builder()
                .id(ticket.getId())
                .eventId(ticket.getEvent().getId())
                .userId(ticket.getUser().getId())
                .status(ticket.getStatus())
                .qrCode(ticket.getQrCode())
                .createdAt(ticket.getCreatedAt())
                .updatedAt(ticket.getUpdatedAt())
                .build();
    }
}
