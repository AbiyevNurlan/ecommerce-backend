package az.edu.itbrains.ecommerce.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CartController {

    // Align new "/cart" route with existing BasketController at "/basket"
    @GetMapping("/cart")
    public String cart() {
        // Delegate to existing basket flow which prepares model attributes
        return "redirect:/basket";
    }

    // Simple checkout page mapping
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/cart/checkout")
    public String checkout() {
        return "cart/checkout"; // resolves to src/main/resources/templates/cart/checkout.html
    }
}
