package com.software.inq.service;

import com.software.inq.dto.UserDTO;
import com.software.inq.mapper.UserMapper;
import com.software.inq.model.Ticket;
import com.software.inq.model.User;
import com.software.inq.repository.TicketRepository;
import com.software.inq.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;

    public List<UserDTO> getAll(){
        return userRepository.findAll().stream()
                .map(UserMapper::toDTO)
                .toList();
    }

    public UserDTO getOne(Long id){
        return userRepository.findById(id)
                .map(UserMapper::toDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id " + id + " does not exist"));
    }

    public UserDTO create(UserDTO userDTO){
        User user = UserMapper.toEntity(userDTO);

        Set<Long> ticketIds = userDTO.ticketIds() != null ? userDTO.ticketIds() : Set.of();

        Set<Ticket> tickets = ticketIds.stream()
                .map(id -> ticketRepository.findById(id)
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "Ticket with id " + id + " not found")))
                .collect(Collectors.toSet());
        user.setTickets(tickets);
        User savedUser = userRepository.save(user);
        return UserMapper.toDTO(savedUser);
    }

    public UserDTO update(Long id, UserDTO userDTO){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User does with id " + id + "does not exist"));

        user.setName(userDTO.name());
        user.setAge(userDTO.age());
        user.setEmailAddress(userDTO.emailAddress());

        if(userDTO.ticketIds() != null && !userDTO.ticketIds().isEmpty()){
            Set<Ticket> tickets = userDTO.ticketIds().stream()
                    .map(ticketId -> ticketRepository.findById(ticketId)
                            .orElseThrow(() -> new ResponseStatusException(
                                    HttpStatus.NOT_FOUND, "Ticket with id " + ticketId + " not found")))
                    .collect(Collectors.toSet());
            user.setTickets(tickets);
        }
        User updatedUser = userRepository.save(user);
        return UserMapper.toDTO(updatedUser);
    }

    public void delete(Long id){
        userRepository.deleteById(id);
    }
}