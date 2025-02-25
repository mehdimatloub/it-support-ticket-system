package com.example.itsupportticket.repository;

import com.example.itsupportticket.entity.AuditLog;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class AuditLogRepositoryTest {

    @Autowired
    private AuditLogRepository auditLogRepository;

    @AfterEach
    public void tearDown() {
        auditLogRepository.deleteAll();
    }

    @Test
    public void testSaveAndDeleteByTicketId() {
        // Crée un log d'audit pour le ticket avec ticketId = 1
        AuditLog auditLog = new AuditLog();
        auditLog.setAction("CREATE");
        auditLog.setEntity("Ticket");
        auditLog.setEntityId(1L);
        auditLog.setDescription("Test log for ticket 1");
        auditLog.setTimestamp(LocalDateTime.now());
        auditLog.setDetails("Some detailed information");
        auditLog.setTicketId(1L);
        auditLog.setUserId(1L);

        // Sauvegarde le log
        AuditLog savedLog = auditLogRepository.save(auditLog);
        assertThat(savedLog.getId()).isNotNull();

        // Vérifie que le log est présent dans la base
        long countBefore = auditLogRepository.findAll().stream()
                .filter(log -> log.getTicketId() == 1L)
                .count();
        assertThat(countBefore).isGreaterThan(0);

        // Supprime les logs associés au ticket d'ID 1
        auditLogRepository.deleteByTicketId(1L);
        auditLogRepository.flush();

        // Vérifie que plus aucun log ne reste pour ce ticket
        long countAfter = auditLogRepository.findAll().stream()
                .filter(log -> log.getTicketId() == 1L)
                .count();
        assertThat(countAfter).isEqualTo(0);
    }
}
