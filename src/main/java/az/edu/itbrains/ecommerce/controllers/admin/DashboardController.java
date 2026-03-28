package az.edu.itbrains.ecommerce.controllers.admin;

import az.edu.itbrains.ecommerce.models.Order;
import az.edu.itbrains.ecommerce.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final SellerRepository sellerRepository;
    private final CategoryRepository categoryRepository;

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("totalProducts", productRepository.count());
        model.addAttribute("totalUsers", userRepository.count());
        model.addAttribute("totalSellers", sellerRepository.count());
        model.addAttribute("totalCategories", categoryRepository.count());

        List<Order> allOrders = orderRepository.findAllByOrderByIdDesc();
        model.addAttribute("totalOrders", allOrders.size());

        double revenue = allOrders.stream()
                .flatMap(o -> o.getOrderItems().stream())
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
        model.addAttribute("totalRevenue", String.format("%.2f", revenue));

        List<Order> recentOrders = allOrders.size() > 5 ? allOrders.subList(0, 5) : allOrders;
        model.addAttribute("recentOrders", recentOrders);

        return "admin/index.html";
    }
}
