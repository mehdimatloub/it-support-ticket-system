package com.example.itsupportticket.service;

import com.example.itsupportticket.entity.Category;
import com.example.itsupportticket.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    // Récupérer toutes les catégories
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // Créer une nouvelle catégorie
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }
}
