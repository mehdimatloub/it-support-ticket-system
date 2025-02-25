package com.example.itsupportticket.repository;

import com.example.itsupportticket.entity.Ticket;
import com.example.itsupportticket.entity.Category;
import com.example.itsupportticket.entity.User;
import com.example.itsupportticket.enums.Priority;
import com.example.itsupportticket.enums.Status;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;
import com.example.itsupportticket.enums.Role;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest  // Charge uniquement la couche JPA
@ActiveProfiles("test")  // Charge application-test.properties
class TicketRepositoryTest {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    private Category category;
    private User user;

    @BeforeEach
    void setup() {
        category = new Category(null, "Software Issue");
        category = categoryRepository.save(category);
        assertThat(category).isNotNull(); // Vérification pour éviter NullPointerException

        user = new User(null, "John Doe", Role.EMPLOYEE);
        user = userRepository.save(user);
        assertThat(user).isNotNull(); // Vérification pour éviter NullPointerException
    }

    @AfterEach
    void tearDown() {
        ticketRepository.deleteAll();
        categoryRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testFindByStatus() {
        // GIVEN
        Ticket ticket1 = new Ticket(null, "Bug in login", "Cannot log in", Priority.High, category, Status.NEW, LocalDateTime.now(), user);
        Ticket ticket2 = new Ticket(null, "UI Issue", "Button misaligned", Priority.Low, category, Status.RESOLVED, LocalDateTime.now(), user);
        ticketRepository.saveAll(List.of(ticket1, ticket2));

        // WHEN
        List<Ticket> newTickets = ticketRepository.findByStatus(Status.NEW);

        // THEN
        assertThat(newTickets).hasSize(1);
        assertThat(newTickets.get(0).getTitle()).isEqualTo("Bug in login");
    }
}
