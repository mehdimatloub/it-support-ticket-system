package com.example.itsupportticket.repository;

import com.example.itsupportticket.entity.Ticket;
import com.example.itsupportticket.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByStatus(Status status);
}
