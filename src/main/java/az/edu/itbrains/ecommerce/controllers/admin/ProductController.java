package az.edu.itbrains.ecommerce.controllers.admin;


import az.edu.itbrains.ecommerce.dtos.category.CategoryDto;
import az.edu.itbrains.ecommerce.dtos.color.ColorDto;
import az.edu.itbrains.ecommerce.dtos.product.ProductCreateDto;
import az.edu.itbrains.ecommerce.dtos.product.ProductDashboardDto;
import az.edu.itbrains.ecommerce.dtos.product.ProductUpdateDto;
import az.edu.itbrains.ecommerce.dtos.size.SizeDto;
import az.edu.itbrains.ecommerce.services.CategoryService;
import az.edu.itbrains.ecommerce.services.ColorService;
import az.edu.itbrains.ecommerce.services.ProductService;
import az.edu.itbrains.ecommerce.services.SizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/dashboard/product")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final ColorService colorService;
    private final SizeService sizeService;


    @GetMapping
    public String index(Model model){
        List<ProductDashboardDto> productDashboardDtoList = productService.getDashboardProducts();
        model.addAttribute("products", productDashboardDtoList);
        return "admin/product/index.html";
    }

    @GetMapping("/create")
    public String create(Model model){
        List<ColorDto> colorDtoList = colorService.getAllColors();
        List<SizeDto> sizeDtoList = sizeService.getAllSizes();
        List<CategoryDto> categoryDtoList = categoryService.getAllCategories();

        model.addAttribute("colors", colorDtoList);
        model.addAttribute("sizes", sizeDtoList);
        model.addAttribute("categories", categoryDtoList);
        return "admin/product/create.html";
    }

    @PostMapping("/create")
    public String create(ProductCreateDto productCreateDto){
        productService.createProduct(productCreateDto);

        return "redirect:/dashboard/product";
    }


    @GetMapping("/update/{id}")
    public String edit(@PathVariable Long id){
        return "admin/product/update.html";
    }

    @PostMapping("/update/{id}")
    public String edit(@PathVariable Long id, ProductUpdateDto productUpdateDto){
        return "redirect:/dashboard/product";
    }




    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        return "admin/product/delete.html";
    }



}
