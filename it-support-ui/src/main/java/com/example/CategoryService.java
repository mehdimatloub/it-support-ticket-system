package com.example;

public class CategoryService {
    public static Category getCategoryByName(String name) {
        Category category = new Category();
        // Assign a dummy id based on the category name.
        // Make sure these ids correspond to what your backend expects.
        switch (name.toLowerCase()) {
            case "network":
                category.setId(1L);
                break;
            case "hardware":
                category.setId(2L);
                break;
            case "software":
                category.setId(3L);
                break;
            case "other":
                category.setId(4L);
                break;
            default:
                throw new IllegalArgumentException("Category not recognized: " + name);
        }
        category.setCategoryName(name);
        return category;
    }
}
