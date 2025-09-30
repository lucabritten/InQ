package com.software.inq.dto;

import com.software.inq.model.TicketStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;


@Builder
public record TicketDTO (
        Long id,
        @NotNull(message = "eventId is required")
        Long eventId,
        @NotNull(message = "userId is required")
        Long userId,
        TicketStatus status,
        String qrCode)
{ }
