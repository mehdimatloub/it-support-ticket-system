package com.example.itsupportticket.service;

import com.example.itsupportticket.entity.AuditLog;
import com.example.itsupportticket.entity.Ticket;
import com.example.itsupportticket.entity.User;
import com.example.itsupportticket.repository.AuditLogRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
@Slf4j
public class AuditLogService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Transactional
    public void logAction(String action, Object entity, User user, String details, String entityName, String description) {
        if (!(entity instanceof Ticket)) {
            throw new IllegalArgumentException("Unsupported entity type");
        }
        Ticket ticket = (Ticket) entity;
        Long entityId = ticket.getId();

        if (description == null || description.isEmpty()) {
            description = String.format("Ticket %d: %s", ticket.getId(), ticket.getTitle());
        }
        if (details == null || details.isEmpty()) {
            details = String.format("Action: %s performed by %s (ID: %d) on ticket ID: %d at %s",
                    action, user.getUsername(), user.getId(), ticket.getId(), LocalDateTime.now());
        }

        AuditLog logEntry = new AuditLog();
        logEntry.setAction(action);
        logEntry.setEntity(entityName);  // "Ticket"
        logEntry.setEntityId(entityId);
        logEntry.setDescription(description);
        logEntry.setTimestamp(LocalDateTime.now());
        logEntry.setDetails(details);
        logEntry.setTicketId(ticket.getId());
        logEntry.setUserId(user.getId());

        try {
            auditLogRepository.save(logEntry);
            auditLogRepository.flush(); // Force flush pour persister immédiatement
            log.info("Successfully created audit log entry for ticket {}", ticket.getId());
        } catch (Exception e) {
            log.error("Error creating audit log: {}", e.getMessage());
            throw e;
        }
    }

    public void flushLogs() {
        auditLogRepository.flush();
    }

    @Transactional
    public void deleteLogsByTicketId(Long ticketId) {
        auditLogRepository.deleteByTicketId(ticketId);
        auditLogRepository.flush();
    }
}
