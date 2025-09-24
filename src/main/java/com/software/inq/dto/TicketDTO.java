package com.software.inq.dto;

import com.software.inq.model.Ticket;
import com.software.inq.model.TicketStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TicketDTO (
        Long id,
        Long eventId,
        Long userId,
        TicketStatus status,
        String qrCode,
        LocalDateTime createdAt,
        LocalDateTime updatedAt)
{ }
