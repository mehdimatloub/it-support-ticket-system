package com.example.itsupportticket.repository;

import com.example.itsupportticket.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    void deleteByTicketId(Long ticketId);
}
