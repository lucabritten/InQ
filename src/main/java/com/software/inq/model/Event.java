package com.software.inq.model;

import jakarta.persistence.Id;

import java.util.Date;

public class Event {
    @Id
    private Long id;
    private String name;
    private String location;
    private Date date;
    private Date created_at;
}
