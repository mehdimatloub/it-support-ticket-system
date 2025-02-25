package com.example.itsupportticket.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "AUDIT_LOG")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ACTION", nullable = false)
    private String action;

    @Column(name = "ENTITY", nullable = false, length = 255)
    private String entity;

    @Column(name = "ENTITY_ID", nullable = false)
    private Long entityId;

    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    @Column(name = "TIMESTAMP", nullable = false)
    private LocalDateTime timestamp;

    @Lob
    @Column(name = "DETAILS")
    private String details;

    @Column(name = "TICKET_ID", nullable = false)
    private Long ticketId;

    @Column(name = "USER_ID", nullable = false)
    private Long userId;
}
