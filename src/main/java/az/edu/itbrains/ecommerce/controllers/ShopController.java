package az.edu.itbrains.ecommerce.controllers;

import az.edu.itbrains.ecommerce.dtos.product.ProductDetailDto;
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

    /**
     * Shop listing with optional full-text search.
     * When a query is supplied, results are sorted by relevance:
     *   - name match ranks highest
     *   - category match ranks second
     *   - description match ranks lowest
     *
     * @param q optional search query (?q=...)
     */
    @GetMapping("/shop")
    public String shop(Model model, @RequestParam(required = false) String q) {
        if (q != null && !q.isBlank()) {
            model.addAttribute("products", productService.searchProducts(q.trim()));
            model.addAttribute("query", q.trim());
        } else {
            model.addAttribute("products", productService.getDashboardProducts());
            model.addAttribute("query", "");
        }
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
