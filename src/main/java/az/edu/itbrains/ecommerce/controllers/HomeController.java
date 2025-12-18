package az.edu.itbrains.ecommerce.controllers;


import az.edu.itbrains.ecommerce.dtos.category.CategoryDto;
import az.edu.itbrains.ecommerce.dtos.product.ProductBestSellerDto;
import az.edu.itbrains.ecommerce.dtos.product.ProductFeaturedDto;
import az.edu.itbrains.ecommerce.dtos.product.ProductHomeDto;
import az.edu.itbrains.ecommerce.dtos.product.ProductHotTrendDto;
import az.edu.itbrains.ecommerce.models.Category;
import az.edu.itbrains.ecommerce.models.Color;
import az.edu.itbrains.ecommerce.repositories.CategoryRepository;
import az.edu.itbrains.ecommerce.services.CategoryService;
import az.edu.itbrains.ecommerce.services.ProductService;
import az.edu.itbrains.ecommerce.services.impls.ProductServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ProductService productService;
    private final CategoryService categoryService;



    @GetMapping("/")
    public String home(Model model) {
        List<ProductHomeDto> products = productService.getAllHomeProducts();
        List<CategoryDto> categoryDtoList = categoryService.getAllCategories();
        List<ProductFeaturedDto> productFeaturedDtoList = productService.getFeaturedProducts();
        List<ProductHotTrendDto> productHotTrendDtoList = productService.getHotTrendProducts();
        List<ProductBestSellerDto> productBestSellerDtoList = productService.getBestSellerProducts();

        model.addAttribute("products", products);
        model.addAttribute("categories", categoryDtoList);
        model.addAttribute("featuredProducts", productFeaturedDtoList);
        model.addAttribute("hotTrendProducts", productHotTrendDtoList);
        return "index.html";
    }
}
