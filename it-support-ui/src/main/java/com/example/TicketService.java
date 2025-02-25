package com.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.hc.client5.http.fluent.Executor;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.util.Timeout;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

public class TicketService {
    private static final String API_URL = "http://localhost:8080/tickets";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "admin123";

    // Récupérer la liste des tickets
    public static List<Ticket> getTickets() throws Exception {
        String authHeader = getAuthHeader();
        String jsonResponse = Executor.newInstance()
                .execute(Request.get(API_URL)
                        .addHeader("Authorization", authHeader)
                        .connectTimeout(Timeout.ofSeconds(10))
                        .responseTimeout(Timeout.ofSeconds(10)))
                .returnContent()
                .asString();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return Arrays.asList(objectMapper.readValue(jsonResponse, Ticket[].class));
    }

    // Créer un ticket (POST)
    public static Ticket createTicket(Ticket ticket) throws Exception {
        String authHeader = getAuthHeader();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String ticketJson = objectMapper.writeValueAsString(ticket);

        String jsonResponse = Executor.newInstance()
                .execute(Request.post(API_URL)
                        .addHeader("Authorization", authHeader)
                        .bodyString(ticketJson, ContentType.APPLICATION_JSON)
                        .connectTimeout(Timeout.ofSeconds(10))
                        .responseTimeout(Timeout.ofSeconds(10)))
                .returnContent()
                .asString();

        return objectMapper.readValue(jsonResponse, Ticket.class);
    }

    // Mettre à jour un ticket (PUT)
    public static Ticket updateTicket(Ticket ticket) throws Exception {
        String authHeader = getAuthHeader();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String ticketJson = objectMapper.writeValueAsString(ticket);

        String jsonResponse = Executor.newInstance()
                .execute(Request.put(API_URL + "/" + ticket.getId()) // PUT request
                        .addHeader("Authorization", authHeader)
                        .bodyString(ticketJson, ContentType.APPLICATION_JSON)
                        .connectTimeout(Timeout.ofSeconds(10))
                        .responseTimeout(Timeout.ofSeconds(10)))
                .returnContent()
                .asString();

        return objectMapper.readValue(jsonResponse, Ticket.class);
    }

    // Supprimer un ticket (DELETE) ✅ AJOUTÉ
    public static void deleteTicket(Long ticketId) throws Exception {
        String authHeader = getAuthHeader();

        Executor.newInstance()
                .execute(Request.delete(API_URL + "/" + ticketId) // DELETE request
                        .addHeader("Authorization", authHeader)
                        .connectTimeout(Timeout.ofSeconds(10))
                        .responseTimeout(Timeout.ofSeconds(10)))
                .discardContent();

        System.out.println("Ticket supprimé avec succès : ID = " + ticketId);
    }

    // Récupérer un utilisateur par username (Dummy)
    public static User getUserByUsername(String username) {
        User user = new User();
        if ("employee".equalsIgnoreCase(username)) {
            user.setId(3L);
            user.setUsername("john");
            user.setRole("EMPLOYEE");
        } else {
            user.setId(1L);
            user.setUsername(username);
            user.setRole("EMPLOYEE");
        }
        return user;
    }

    // Générer le header d'authentification Basic Auth
    private static String getAuthHeader() {
        String credentials = USERNAME + ":" + PASSWORD;
        return "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
    }
}
