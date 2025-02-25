package com.example.itsupportticket.service;

import com.example.itsupportticket.entity.Category;
import com.example.itsupportticket.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category category1;
    private Category category2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        category1 = new Category(1L, "Software");
        category2 = new Category(2L, "Hardware");
    }

    @Test
    void testGetAllCategories() {
        when(categoryRepository.findAll()).thenReturn(Arrays.asList(category1, category2));

        List<Category> categories = categoryService.getAllCategories();

        assertThat(categories).hasSize(2);
        assertThat(categories.get(0).getCategoryName()).isEqualTo("Software");
        assertThat(categories.get(1).getCategoryName()).isEqualTo("Hardware");

        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void testCreateCategory() {
        when(categoryRepository.save(category1)).thenReturn(category1);

        Category createdCategory = categoryService.createCategory(category1);

        assertThat(createdCategory).isNotNull();
        assertThat(createdCategory.getCategoryName()).isEqualTo("Software");

        verify(categoryRepository, times(1)).save(category1);
    }
}
