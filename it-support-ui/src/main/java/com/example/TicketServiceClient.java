package com.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class TicketServiceClient {
    private static final String BASE_URL = "http://localhost:8080/tickets";
    private static final OkHttpClient client = new OkHttpClient();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Identifiants pour l'authentification (remplace par les vrais)
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "admin123";

    private static String getAuthHeader() {
        String credentials = USERNAME + ":" + PASSWORD;
        return "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
    }

    public static Ticket createTicket(Ticket ticket) throws IOException {
        RequestBody body = RequestBody.create(
                objectMapper.writeValueAsString(ticket),
                MediaType.get("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(BASE_URL)
                .post(body)
                .addHeader("Authorization", getAuthHeader())  // Ajout de l'authentification
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            return objectMapper.readValue(response.body().string(), Ticket.class);
        }
    }
}
