package az.edu.itbrains.ecommerce.controllers.admin;

import az.edu.itbrains.ecommerce.dtos.category.CategoryDto;
import az.edu.itbrains.ecommerce.dtos.category.CategoryCreateDto;
import az.edu.itbrains.ecommerce.dtos.category.CategoryUpdateDto;
import az.edu.itbrains.ecommerce.services.CategoryService;
import az.edu.itbrains.ecommerce.services.CategoryService;
import az.edu.itbrains.ecommerce.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/dashboard/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;


    @GetMapping
    public String index(Model model){
        List<CategoryDto> categoryDtoList = categoryService.getAllCategories();
        model.addAttribute("categories", categoryDtoList);
        return "admin/category/index.html";
    }

    @GetMapping("/create")
    public String create(){
        return "admin/category/create.html";
    }

    @PostMapping("/create")
    public String create(CategoryCreateDto categoryCreateDto){
        boolean result = categoryService.saveCategory(categoryCreateDto);
        return "redirect:/dashboard/category";
    }


    @GetMapping("/update/{id}")
    public String edit(@PathVariable Long id, Model model){
        CategoryUpdateDto categoryUpdateDto = categoryService.findUpdatedCategory(id);
        model.addAttribute("category", categoryUpdateDto);
        return "admin/category/update.html";
    }

    @PostMapping("/update/{id}")
    public String edit(@PathVariable Long id, CategoryUpdateDto categoryUpdateDto){
        boolean result = categoryService.updateCategory(id, categoryUpdateDto);
        return "redirect:/dashboard/category";
    }




    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        return "admin/category/delete.html";
    }

}
