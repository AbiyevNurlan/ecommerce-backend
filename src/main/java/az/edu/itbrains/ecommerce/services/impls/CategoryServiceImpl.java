package az.edu.itbrains.ecommerce.services.impls;

import az.edu.itbrains.ecommerce.dtos.category.CategoryCreateDto;
import az.edu.itbrains.ecommerce.dtos.category.CategoryDto;
import az.edu.itbrains.ecommerce.dtos.category.CategoryUpdateDto;
import az.edu.itbrains.ecommerce.exceptions.ResourceNotFoundException;
import az.edu.itbrains.ecommerce.exceptions.ServiceException;
import az.edu.itbrains.ecommerce.models.Category;
import az.edu.itbrains.ecommerce.repositories.CategoryRepository;
import az.edu.itbrains.ecommerce.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private static final Logger log = LoggerFactory.getLogger(CategoryServiceImpl.class);

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;


    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(category -> modelMapper.map(category, CategoryDto.class))
                .toList();
    }

    @Override
    @Transactional
    public boolean saveCategory(CategoryCreateDto categoryCreateDto) {
        try {
            Category category = new Category();
            category.setName(categoryCreateDto.getName());
            categoryRepository.save(category);
            return true;
        } catch (Exception e) {
            log.error("Failed to save category: {}", categoryCreateDto.getName(), e);
            throw new ServiceException("Failed to save category: " + categoryCreateDto.getName(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryUpdateDto findUpdatedCategory(Long id) {
        Objects.requireNonNull(id, "Category id must not be null");
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id, "Category"));
        return modelMapper.map(category, CategoryUpdateDto.class);
    }

    @Override
    @Transactional
    public boolean updateCategory(Long id, CategoryUpdateDto categoryUpdateDto) {
        Objects.requireNonNull(id, "Category id must not be null");
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id, "Category"));
        category.setName(categoryUpdateDto.getName());
        categoryRepository.save(category);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public Category getCategoryById(Long categoryId) {
        Objects.requireNonNull(categoryId, "Category id must not be null");
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(categoryId, "Category"));
    }

    @Override
    @Transactional
    public boolean deleteCategory(Long id) {
        Objects.requireNonNull(id, "Category id must not be null");
        categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id, "Category"));
        try {
            categoryRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            log.error("Failed to delete category with id: {}", id, e);
            throw new ServiceException("Failed to delete category with id: " + id, e);
        }
    }
}
