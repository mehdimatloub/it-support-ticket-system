package com.example.itsupportticket.repository;

import com.example.itsupportticket.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Tu peux ajouter des méthodes personnalisées ici si nécessaire
    User findByUsername(String username);
}
