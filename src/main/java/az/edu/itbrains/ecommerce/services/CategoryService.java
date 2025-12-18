package az.edu.itbrains.ecommerce.services;

import az.edu.itbrains.ecommerce.dtos.category.CategoryCreateDto;
import az.edu.itbrains.ecommerce.dtos.category.CategoryDto;
import az.edu.itbrains.ecommerce.dtos.category.CategoryUpdateDto;
import az.edu.itbrains.ecommerce.models.Category;

import java.util.List;

public interface CategoryService {

    List<CategoryDto> getAllCategories();

    boolean saveCategory(CategoryCreateDto categoryCreateDto);

    CategoryUpdateDto findUpdatedCategory(Long id);

    boolean updateCategory(Long id, CategoryUpdateDto categoryUpdateDto);

    Category getCategoryById(Long categoryId);
}
