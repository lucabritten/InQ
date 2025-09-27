package com.software.inq.service;

import com.software.inq.dto.TicketDTO;
import com.software.inq.mapper.TicketMapper;
import com.software.inq.repository.TicketRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;

    public List<TicketDTO> getAll(){
        return ticketRepository.findAll()
                .stream()
                .map(TicketMapper::toDTO)
                .toList();
    }

    public TicketDTO getOne(Long id){
        return null;
    }

    public TicketDTO create(TicketDTO ticketDTO){
        return null;
    }

    public TicketDTO update(Long id, TicketDTO ticketDTO){
        return null;
    }

    public void delete(Long id){
        ticketRepository.deleteById(id);
    }
}
