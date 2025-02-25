package com.example.itsupportticket.controller;

import com.example.itsupportticket.entity.Category;
import com.example.itsupportticket.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CategoryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    private Category category1;
    private Category category2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();

        category1 = new Category(1L, "Software");
        category2 = new Category(2L, "Hardware");
    }

    // ✅ Test GET /api/categories
    @Test
    void testGetAllCategories() throws Exception {
        List<Category> categories = Arrays.asList(category1, category2);
        when(categoryService.getAllCategories()).thenReturn(categories);

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2)) // Vérifie que la liste a bien 2 éléments
                .andExpect(jsonPath("$[0].categoryName").value("Software"))  // Changer `name` en `categoryName`
                .andExpect(jsonPath("$[1].categoryName").value("Hardware"));

        verify(categoryService, times(1)).getAllCategories();
    }

    // ✅ Test POST /api/categories
    @Test
    void testCreateCategory() throws Exception {
        when(categoryService.createCategory(any(Category.class))).thenReturn(category1);

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"categoryName\": \"Software\"}"))  // Changer `name` en `categoryName`
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryName").value("Software"));

        verify(categoryService, times(1)).createCategory(any(Category.class));
    }
}
