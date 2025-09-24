package com.software.inq.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "ticket")
public class Ticket {

    @Id
    private Long id;
    private Long event_id;
    @ManyToOne
    private User user;
    private TicketStatus status;
    private String qr_code;
    private Date created_at;
}
