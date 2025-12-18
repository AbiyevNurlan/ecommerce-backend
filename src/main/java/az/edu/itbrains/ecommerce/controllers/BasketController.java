package az.edu.itbrains.ecommerce.controllers;

import az.edu.itbrains.ecommerce.dtos.basket.BasketAddDto;
import az.edu.itbrains.ecommerce.dtos.basket.BasketUserDto;
import az.edu.itbrains.ecommerce.services.BasketService;
import az.edu.itbrains.ecommerce.services.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/basket")
@RequiredArgsConstructor
public class BasketController {


    private final BasketService basketService;
    private final UserService userService;



    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public String basket(Principal principal, Model model){
        String email = principal.getName();

        List<BasketUserDto> basketUserDtoList = userService.getUserBasket(email);

        Double totalPrice = basketUserDtoList.stream().mapToDouble(BasketUserDto::getTotalPrice).sum();
        model.addAttribute("totalPrice", totalPrice);

        model.addAttribute("baskets", basketUserDtoList);

        return "cart/shop-cart.html";
    }

    @PostMapping("/addToCart")
    @PreAuthorize("isAuthenticated()")
    public String addToCart(BasketAddDto basketAddDto, Principal principal){
        String email = principal.getName();
        basketService.addToCart(email, basketAddDto);

        return "redirect:/basket";
    }

    @GetMapping("/remove/{productId}")
    @PreAuthorize("isAuthenticated()")
    public String remove(@PathVariable Long productId, Principal principal){
        String email = principal.getName();
        boolean result = basketService.removeFromBasket(email, productId);

        return "redirect:/basket";
    }
}
