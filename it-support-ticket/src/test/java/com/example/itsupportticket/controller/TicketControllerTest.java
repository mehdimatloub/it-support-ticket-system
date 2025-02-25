package com.example.itsupportticket.controller;

import com.example.itsupportticket.entity.Ticket;
import com.example.itsupportticket.enums.Priority;
import com.example.itsupportticket.enums.Status;
import com.example.itsupportticket.service.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TicketControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TicketService ticketService;

    @InjectMocks
    private TicketController ticketController;

    private Ticket ticket1;
    private Ticket ticket2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(ticketController).build();

        // Pour les tests, on crée deux tickets avec des valeurs de test
        ticket1 = new Ticket(1L, "Problème logiciel", "Erreur dans l'application", Priority.High, Status.NEW, null, null);
        ticket2 = new Ticket(2L, "Problème matériel", "Écran cassé", Priority.Medium, Status.IN_PROGRESS, null, null);
    }

    @Test
    void testGetAllTickets() throws Exception {
        when(ticketService.getAllTickets()).thenReturn(Arrays.asList(ticket1, ticket2));

        mockMvc.perform(get("/tickets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].title").value("Problème logiciel"))
                .andExpect(jsonPath("$[1].title").value("Problème matériel"));

        verify(ticketService, times(1)).getAllTickets();
    }

    @Test
    void testGetTicketById_Found() throws Exception {
        when(ticketService.getTicketById(1L)).thenReturn(ticket1);

        mockMvc.perform(get("/tickets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Problème logiciel"));

        verify(ticketService, times(1)).getTicketById(1L);
    }

    @Test
    void testGetTicketById_NotFound() throws Exception {
        // Ici, on configure le mock pour lancer une exception, comme dans le service
        when(ticketService.getTicketById(1L))
                .thenThrow(new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Ticket not found with id: 1"));

        mockMvc.perform(get("/tickets/1"))
                .andExpect(status().isNotFound());

        verify(ticketService, times(1)).getTicketById(1L);
    }

    @Test
    void testCreateTicket() throws Exception {
        when(ticketService.createTicket(any(Ticket.class))).thenReturn(ticket1);

        mockMvc.perform(post("/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"Problème logiciel\", \"description\": \"Erreur dans l'application\", \"priority\": \"High\", \"status\": \"NEW\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Problème logiciel"));

        verify(ticketService, times(1)).createTicket(any(Ticket.class));
    }

    @Test
    void testUpdateTicket() throws Exception {
        when(ticketService.updateTicket(eq(1L), any(Ticket.class))).thenReturn(ticket1);

        mockMvc.perform(put("/tickets/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"Problème logiciel\", \"description\": \"Erreur mise à jour\", \"priority\": \"High\", \"status\": \"IN_PROGRESS\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Problème logiciel"));

        verify(ticketService, times(1)).updateTicket(eq(1L), any(Ticket.class));
    }

    @Test
    void testDeleteTicket() throws Exception {
        doNothing().when(ticketService).deleteTicket(1L);

        mockMvc.perform(delete("/tickets/1"))
                .andExpect(status().isNoContent());

        verify(ticketService, times(1)).deleteTicket(1L);
    }
}
