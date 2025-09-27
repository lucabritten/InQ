package com.software.inq.mapper;

import com.software.inq.dto.TicketDTO;
import com.software.inq.model.Ticket;

public class TicketMapper {

    private TicketMapper(){

    }

    public static TicketDTO toDTO(Ticket ticket){
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

    /**
     * User and Event needs to be set in service class
     */
    public static Ticket toEntity(TicketDTO dto){
        Ticket ticket = new Ticket();
        ticket.setId(dto.id());
        ticket.setStatus(dto.status());
        ticket.setQrCode(dto.qrCode());

        return ticket;
    }
}
