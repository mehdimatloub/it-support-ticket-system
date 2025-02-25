package com.example.itsupportticket.service;

import com.example.itsupportticket.entity.*;
import com.example.itsupportticket.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AuditLogService auditLogService; // Service d'audit

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public Ticket getTicketById(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket not found with id: " + id));
    }

    @Transactional
    public Ticket createTicket(Ticket ticket) {
        User user = userRepository.findById(ticket.getUser().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found."));
        Category category = categoryRepository.findById(ticket.getCategory().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category not found."));

        ticket.setUser(user);
        ticket.setCategory(category);
        ticket.setCreatedAt(LocalDateTime.now());
        Ticket savedTicket = ticketRepository.save(ticket);

        String description = String.format("Created new ticket: %s (ID: %d)", savedTicket.getTitle(), savedTicket.getId());
        String details = String.format("Ticket created by %s on %s", user.getUsername(), LocalDateTime.now());
        auditLogService.logAction("CREATE", savedTicket, user, details, "Ticket", description);

        return savedTicket;
    }

    @Transactional
    public Ticket updateTicket(Long id, Ticket updatedTicket) {
        Ticket existingTicket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket not found with id: " + id));

        existingTicket.setTitle(updatedTicket.getTitle());
        existingTicket.setDescription(updatedTicket.getDescription());
        existingTicket.setPriority(updatedTicket.getPriority());

        // Recharger la catégorie depuis la base de données
        Category category = categoryRepository.findById(updatedTicket.getCategory().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category not found."));
        existingTicket.setCategory(category);

        existingTicket.setStatus(updatedTicket.getStatus());

        Ticket savedTicket = ticketRepository.save(existingTicket);

        String description = String.format("Updated ticket: %s (ID: %d)", savedTicket.getTitle(), savedTicket.getId());
        String details = String.format("Ticket updated by %s on %s", existingTicket.getUser().getUsername(), LocalDateTime.now());
        auditLogService.logAction("UPDATE", savedTicket, existingTicket.getUser(), details, "Ticket", description);

        return savedTicket;
    }

    @Transactional
    public void deleteTicket(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket not found with id: " + id));

        User user = ticket.getUser();

        // Enregistrer le log d'audit AVANT la suppression
        String description = String.format("Deleted ticket: %s (ID: %d)", ticket.getTitle(), ticket.getId());
        String details = String.format("Ticket deleted by %s on %s", user.getUsername(), LocalDateTime.now());
        auditLogService.logAction("DELETE", ticket, user, details, "Ticket", description);
        auditLogService.flushLogs();

        // Supprimer explicitement les logs d'audit associés au ticket via le service d'audit
        auditLogService.deleteLogsByTicketId(ticket.getId());

        // Ensuite, supprimer le ticket
        ticketRepository.delete(ticket);
    }
}