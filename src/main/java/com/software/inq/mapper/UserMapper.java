package com.software.inq.mapper;

import com.software.inq.dto.UserDTO;
import com.software.inq.model.Ticket;
import com.software.inq.model.User;

import java.util.stream.Collectors;

public class UserMapper {

    private UserMapper(){

    }

    public static UserDTO toDTO(User user){
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .age(user.getAge())
                .emailAddress(user.getEmailAddress())
                .ticketIds(user.getTickets().stream()
                        .map(Ticket::getId)
                        .collect(Collectors.toSet()))
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    /**
     * field tickets needs to be set in service class
     */
    public static User getEntity(UserDTO dto){
        User user = new User();
        user.setId(dto.id());
        user.setAge(dto.age());
        user.setEmailAddress(dto.emailAddress());
        return user;
    }
}
