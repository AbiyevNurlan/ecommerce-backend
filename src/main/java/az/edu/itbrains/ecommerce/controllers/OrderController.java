package az.edu.itbrains.ecommerce.controllers;

import az.edu.itbrains.ecommerce.dtos.order.OrderDto;
import az.edu.itbrains.ecommerce.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    public String myOrders(Model model, Principal principal) {
        List<OrderDto> orders = orderService.getUserOrders(principal.getName());
        model.addAttribute("orders", orders);
        return "orders/my-orders";
    }
}
