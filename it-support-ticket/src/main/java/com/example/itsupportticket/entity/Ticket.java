package com.example.itsupportticket.entity;

import com.example.itsupportticket.enums.Priority;
import com.example.itsupportticket.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "TICKETS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ticket_seq")
    @SequenceGenerator(name = "ticket_seq", sequenceName = "TICKET_SEQ", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority;  // HIGH, MEDIUM, LOW

    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID", nullable = false)
    private Category category;  // Relation avec la table des catégories

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.NEW;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;  // Relation avec l'utilisateur

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
    // ✅ Ajout du constructeur sans createdAt
    public Ticket(Long id, String title, String description, Priority priority, Status status, User user, Category category) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.user = user;
        this.category = category;
        this.createdAt = LocalDateTime.now(); // Pour éviter null
    }
}
