package com.software.inq.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record EventDTO(Long id,
                       String name,
                       String location,
                       LocalDateTime date,
                       Set<Long> ticketIds)
{ }
