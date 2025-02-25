package com.example.itsupportticket.repository;

import com.example.itsupportticket.entity.Category;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest  // Charge uniquement la couche JPA pour les tests
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    private Category category;

    @BeforeEach
    void setUp() {
        // Préparer une catégorie avant chaque test
        category = new Category();
        category.setCategoryName("Network");
        category = categoryRepository.save(category);
    }

    @AfterEach
    void tearDown() {
        // Nettoyer la base après chaque test
        categoryRepository.deleteAll();
    }

    @Test
    void testSaveCategory() {
        // Vérifier que la catégorie a bien été enregistrée
        Optional<Category> foundCategory = categoryRepository.findById(category.getId());
        assertThat(foundCategory).isPresent();
        assertThat(foundCategory.get().getCategoryName()).isEqualTo("Network");
    }

    @Test
    void testFindById() {
        // Vérifier que la recherche par ID fonctionne
        Optional<Category> foundCategory = categoryRepository.findById(category.getId());
        assertThat(foundCategory).isPresent();
    }

    @Test
    void testDeleteCategory() {
        // Supprimer et vérifier que la catégorie n'existe plus
        categoryRepository.delete(category);
        Optional<Category> foundCategory = categoryRepository.findById(category.getId());
        assertThat(foundCategory).isNotPresent();
    }
}
