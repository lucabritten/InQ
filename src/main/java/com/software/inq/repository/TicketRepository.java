package com.software.inq.repository;

import com.software.inq.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    boolean existsByUserIdAndEventId(Long userId, Long eventId);
}
