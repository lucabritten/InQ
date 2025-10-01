package com.software.inq.service;

import com.software.inq.dto.TicketDTO;
import com.software.inq.mapper.TicketMapper;
import com.software.inq.model.Event;
import com.software.inq.model.Ticket;
import com.software.inq.model.TicketStatus;
import com.software.inq.model.User;
import com.software.inq.util.PdfUtil;
import com.software.inq.util.QRCodeUtil;
import com.software.inq.repository.EventRepository;
import com.software.inq.repository.TicketRepository;
import com.software.inq.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    public List<TicketDTO> getAll(){
        return ticketRepository.findAll()
                .stream()
                .map(TicketMapper::toDTO)
                .toList();
    }

    public TicketDTO getOne(Long id){
        return ticketRepository.findById(id)
                .map(TicketMapper::toDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket with id " + id + " does not exist."));
    }

    public TicketDTO create(TicketDTO ticketDTO){
        Ticket ticket = TicketMapper.toEntity(ticketDTO);

        Event linkedEvent = getLinkedEvent(ticketDTO.eventId());
        if(linkedEvent.getDate().isBefore(LocalDateTime.now()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot create ticket for past Event.");
        ticket.setEvent(linkedEvent);

        User linkedUser = getLinkedUser(ticketDTO.userId());
        boolean alreadyHasTicket = ticketRepository.existsByUserIdAndEventId(linkedUser.getId(), ticket.getEvent().getId());
        if(alreadyHasTicket)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already has a ticket for this event.");
        ticket.setUser(linkedUser);

        ticket.setStatus(TicketStatus.VALID);

        Ticket saved = ticketRepository.save(ticket);

        saved.setQrCode(QRCodeUtil.generateQRCodeBase64(
                saved.getUser().getId(),
                saved.getEvent().getId(),
                saved.getId()
        ));

        Ticket finalSavedTicket = ticketRepository.save(saved);
        return TicketMapper.toDTO(finalSavedTicket);
    }

    public TicketDTO update(Long id, TicketDTO ticketDTO){
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Ticket with id " + id + " not found."));

        ticket.setEvent(getLinkedEvent(ticketDTO.eventId()));
        ticket.setUser(getLinkedUser(ticketDTO.userId()));
        ticket.setStatus(ticketDTO.status());
        ticket.setQrCode(ticketDTO.qrCode());

        Ticket updatedTicket = ticketRepository.save(ticket);
        return TicketMapper.toDTO(updatedTicket);
    }

    public void delete(Long id){
        ticketRepository.deleteById(id);
    }

    public byte[] generateTicketPdf(Long ticketId) {
        TicketDTO ticket = getOne(ticketId);
        String eventName = eventRepository.findById(ticket.eventId())
                .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Event with id " + ticket.eventId() + " not found."))
                .getName();
        String userName = userRepository.findById(ticket.userId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User with id " + ticket.userId() + " not found."))
                .getName();

        String userEmail = userRepository.findById(ticket.userId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User with id " + ticket.userId() + " not found."))
                .getEmailAddress();

        LocalDateTime eventDate = eventRepository.findById(ticket.eventId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Event with id " + ticket.eventId() + " not found."))
                .getDate();

        try {
            return PdfUtil.generateTicketPdf(eventName, eventDate,userName, userEmail,ticket.id(), ticket.qrCode());
        } catch (Exception e) {
            throw new RuntimeException("Fehler beim Generieren des PDFs", e);
        }
    }

    public TicketDTO useTicket(Long id){
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket with id " + id + " not found."));

        if(ticket.getStatus() != TicketStatus.VALID)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Ticket with status " + ticket.getStatus() + " cannot be used.");

        ticket.setStatus(TicketStatus.USED);
        System.out.println("TICKETSTATUS: " + ticket.getStatus());
        Ticket savedTicket = ticketRepository.save(ticket);
        return TicketMapper.toDTO(savedTicket);
    }

    private Event getLinkedEvent(Long eventId){
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,"Event with id " + eventId + " not found."));
    }

    private User getLinkedUser(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User with id " + userId + " not found."));

    }
}
