package com.example.itsupportticket.service;

import com.example.itsupportticket.entity.AuditLog;
import com.example.itsupportticket.entity.Category;
import com.example.itsupportticket.entity.Ticket;
import com.example.itsupportticket.entity.User;
import com.example.itsupportticket.enums.Priority;
import com.example.itsupportticket.enums.Status;
import com.example.itsupportticket.repository.CategoryRepository;
import com.example.itsupportticket.repository.TicketRepository;
import com.example.itsupportticket.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private AuditLogService auditLogService;

    @InjectMocks
    private TicketService ticketService;

    private Ticket ticket;
    private User user;
    private Category category;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User(1L, "john.doe", com.example.itsupportticket.enums.Role.EMPLOYEE);
        category = new Category(1L, "Software");
        // Supposons que le constructeur de Ticket ressemble à :
        // Ticket(Long id, String title, String description, Priority priority, Status status, User user, Category category)
        ticket = new Ticket(1L, "Bug", "App crashes", Priority.High, Status.NEW, user, category);
        ticket.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void testGetAllTickets() {
        when(ticketRepository.findAll()).thenReturn(List.of(ticket));
        List<Ticket> tickets = ticketService.getAllTickets();
        assertThat(tickets).hasSize(1);
        assertThat(tickets.get(0).getTitle()).isEqualTo("Bug");
        verify(ticketRepository, times(1)).findAll();
    }

    @Test
    void testGetTicketById_Found() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        Ticket foundTicket = ticketService.getTicketById(1L);
        assertThat(foundTicket).isNotNull();
        assertThat(foundTicket.getTitle()).isEqualTo("Bug");
        verify(ticketRepository, times(1)).findById(1L);
    }

    @Test
    void testGetTicketById_NotFound() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> ticketService.getTicketById(1L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Ticket not found with id: 1");
    }

    @Test
    void testCreateTicket_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);
        Ticket createdTicket = ticketService.createTicket(ticket);
        assertThat(createdTicket.getTitle()).isEqualTo("Bug");
        verify(ticketRepository, times(1)).save(ticket);
        verify(auditLogService, times(1))
                .logAction(eq("CREATE"), any(Ticket.class), eq(user), anyString(), eq("Ticket"), anyString());
    }

    @Test
    void testUpdateTicket_Success() {
        Ticket updatedTicket = new Ticket(1L, "Updated Bug", "New description", Priority.Medium, Status.IN_PROGRESS, user, category);

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category)); // Ajouté
        when(ticketRepository.save(any(Ticket.class))).thenReturn(updatedTicket);

        Ticket result = ticketService.updateTicket(1L, updatedTicket);

        assertThat(result.getTitle()).isEqualTo("Updated Bug");
        assertThat(result.getPriority()).isEqualTo(Priority.Medium);

        verify(ticketRepository, times(1)).save(any(Ticket.class));
        verify(auditLogService, times(1))
                .logAction(eq("UPDATE"), any(Ticket.class), eq(user), anyString(), eq("Ticket"), anyString());
    }

    @Test
    void testUpdateTicket_NotFound() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> ticketService.updateTicket(1L, ticket))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Ticket not found with id: 1");
    }

    @Test
    void testDeleteTicket_Success() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        doNothing().when(ticketRepository).delete(ticket);
        doNothing().when(auditLogService).logAction(anyString(), any(), any(User.class), anyString(), anyString(), anyString());
        doNothing().when(auditLogService).flushLogs();
        doNothing().when(auditLogService).deleteLogsByTicketId(anyLong());

        ticketService.deleteTicket(1L);

        verify(auditLogService, times(1))
                .logAction(eq("DELETE"), any(Ticket.class), eq(user), anyString(), eq("Ticket"), anyString());
        verify(auditLogService, times(1)).flushLogs();
        verify(auditLogService, times(1)).deleteLogsByTicketId(ticket.getId());
        verify(ticketRepository, times(1)).delete(ticket);
    }

    @Test
    void testDeleteTicket_NotFound() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> ticketService.deleteTicket(1L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Ticket not found with id: 1");
    }
}
