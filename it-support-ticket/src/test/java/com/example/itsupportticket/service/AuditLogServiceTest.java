package com.example.itsupportticket.service;

import com.example.itsupportticket.entity.AuditLog;
import com.example.itsupportticket.entity.Ticket;
import com.example.itsupportticket.entity.User;
import com.example.itsupportticket.repository.AuditLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AuditLogServiceTest {

    @Mock
    private AuditLogRepository auditLogRepository;

    @InjectMocks
    private AuditLogService auditLogService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogAction() {
        // Création d'un utilisateur et d'un ticket factices
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        Ticket ticket = new Ticket();
        ticket.setId(100L);
        ticket.setTitle("Test Ticket");

        String action = "CREATE";
        String details = "Ticket created by testuser at " + LocalDateTime.now();
        String entityName = "Ticket";
        String description = "Test description";

        // Lorsque save est appelé, on renvoie l'objet passé
        when(auditLogRepository.save(any(AuditLog.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Appel de la méthode logAction
        auditLogService.logAction(action, ticket, user, details, entityName, description);

        // Capture de l'objet AuditLog sauvegardé pour vérification
        ArgumentCaptor<AuditLog> captor = ArgumentCaptor.forClass(AuditLog.class);
        verify(auditLogRepository, times(1)).save(captor.capture());
        AuditLog savedLog = captor.getValue();

        assertThat(savedLog.getAction()).isEqualTo(action);
        assertThat(savedLog.getEntity()).isEqualTo(entityName);
        assertThat(savedLog.getEntityId()).isEqualTo(ticket.getId());
        assertThat(savedLog.getDescription()).isEqualTo(description);
        assertThat(savedLog.getTicketId()).isEqualTo(ticket.getId());
        assertThat(savedLog.getUserId()).isEqualTo(user.getId());
    }

    @Test
    void testDeleteLogsByTicketId() {
        Long ticketId = 100L;
        doNothing().when(auditLogRepository).deleteByTicketId(ticketId);
        doNothing().when(auditLogRepository).flush();

        auditLogService.deleteLogsByTicketId(ticketId);

        verify(auditLogRepository, times(1)).deleteByTicketId(ticketId);
        verify(auditLogRepository, times(1)).flush();
    }
}
