package az.edu.itbrains.ecommerce.controllers;

import az.edu.itbrains.ecommerce.dtos.basket.BasketUserDto;
import az.edu.itbrains.ecommerce.dtos.order.PlaceOrderDto;
import az.edu.itbrains.ecommerce.exceptions.ServiceException;
import az.edu.itbrains.ecommerce.services.OrderService;
import az.edu.itbrains.ecommerce.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final OrderService orderService;
    private final UserService userService;

    @GetMapping("/cart")
    public String cart() {
        return "redirect:/basket";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/cart/checkout")
    public String checkout(Model model, Principal principal) {
        List<BasketUserDto> baskets = userService.getUserBasket(principal.getName());
        double totalPrice = baskets.stream().mapToDouble(BasketUserDto::getTotalPrice).sum();
        model.addAttribute("order", new PlaceOrderDto());
        model.addAttribute("baskets", baskets);
        model.addAttribute("totalPrice", totalPrice);
        return "cart/checkout";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/cart/checkout")
    public String processCheckout(@Valid @ModelAttribute("order") PlaceOrderDto placeOrderDto,
                                  BindingResult result,
                                  Principal principal,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            List<BasketUserDto> baskets = userService.getUserBasket(principal.getName());
            double totalPrice = baskets.stream().mapToDouble(BasketUserDto::getTotalPrice).sum();
            model.addAttribute("baskets", baskets);
            model.addAttribute("totalPrice", totalPrice);
            return "cart/checkout";
        }
        try {
            orderService.placeOrder(principal.getName(), placeOrderDto);
            redirectAttributes.addFlashAttribute("success", "Sifarişiniz uğurla qəbul edildi!");
            return "redirect:/orders/my";
        } catch (ServiceException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/cart/checkout";
        }
    }
}

