package az.edu.itbrains.ecommerce.controllers;

import az.edu.itbrains.ecommerce.dtos.product.ProductDetailDto;
import az.edu.itbrains.ecommerce.services.CategoryService;
import az.edu.itbrains.ecommerce.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class ShopController {

    private final ProductService productService;
    private final CategoryService categoryService;

    /**
     * Shop listing with optional category filter and full-text search.
     *
     * @param category optional category seoUrl filter (?category=women)
     * @param q        optional search query (?q=...)
     */
    @GetMapping("/shop")
    public String shop(Model model,
                       @RequestParam(required = false) String category,
                       @RequestParam(required = false) String q) {

        if (q != null && !q.isBlank()) {
            model.addAttribute("products", productService.searchProducts(q.trim()));
            model.addAttribute("query", q.trim());
        } else if (category != null && !category.isBlank()) {
            model.addAttribute("products", productService.getProductsByCategory(category.trim()));
            model.addAttribute("selectedCategory", category.trim());
            model.addAttribute("query", "");
        } else {
            model.addAttribute("products", productService.getDashboardProducts());
            model.addAttribute("query", "");
        }

        model.addAttribute("categories", categoryService.getAllCategories());
        return "shop/shop.html";
    }

    /**
     * Product detail page.
     * In addition to product data, passes collaborative-filtering recommendations
     * computed by RecommendationService (frequently bought together).
     */
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model) {
        ProductDetailDto productDetailDto = productService.getProductDetail(id);
        model.addAttribute("product", productDetailDto);
        model.addAttribute("recommendations", productService.getRecommendations(id));
        return "shop/detail.html";
    }
}
