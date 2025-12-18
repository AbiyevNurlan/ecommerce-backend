package az.edu.itbrains.ecommerce.services.impls;

import az.edu.itbrains.ecommerce.dtos.category.CategoryCreateDto;
import az.edu.itbrains.ecommerce.dtos.category.CategoryDto;
import az.edu.itbrains.ecommerce.dtos.category.CategoryUpdateDto;
import az.edu.itbrains.ecommerce.models.Category;
import az.edu.itbrains.ecommerce.repositories.CategoryRepository;
import az.edu.itbrains.ecommerce.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;


    @Override
    public List<CategoryDto> getAllCategories() {
        List<Category> categorys = categoryRepository.findAll();
        if (!categorys.isEmpty()) {
            return categorys.stream().map(category -> modelMapper.map(category, CategoryDto.class)).toList();
        }
        return List.of();
    }

    @Override
    public boolean saveCategory(CategoryCreateDto categoryCreateDto) {
        try {
            Category category = new Category();
            category.setName(categoryCreateDto.getName());
            categoryRepository.save(category);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public CategoryUpdateDto findUpdatedCategory(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow();
        CategoryUpdateDto categoryUpdateDto = modelMapper.map(category, CategoryUpdateDto.class);
        return categoryUpdateDto;
    }

    @Override
    public boolean updateCategory(Long id, CategoryUpdateDto categoryUpdateDto) {
        Category category = categoryRepository.findById(id).orElseThrow();
        category.setName(categoryUpdateDto.getName());
        categoryRepository.save(category);
        return true;
    }

    @Override
    public Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow();
    }
}
