package com.example.itsupportticket.repository;

import com.example.itsupportticket.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    // Tu peux ajouter des méthodes personnalisées ici si nécessaire
}
