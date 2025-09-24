package com.software.inq.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record UserDTO(Long id,
                      String name,
                      Integer age,
                      String emailAddress,
                      Set<Long> ticket_ids,
                      LocalDateTime createdAt,
                      LocalDateTime updatedAt)
{

}
