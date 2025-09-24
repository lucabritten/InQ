package com.software.inq.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record EventDTO(Long id,
                       String name,
                       String location,
                       LocalDateTime date)
{ }
