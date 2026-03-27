package az.edu.itbrains.ecommerce.controllers.admin;

import az.edu.itbrains.ecommerce.enums.OrderStatus;
import az.edu.itbrains.ecommerce.exceptions.ResourceNotFoundException;
import az.edu.itbrains.ecommerce.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/dashboard/order")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderService orderService;

    @GetMapping
    public String listOrders(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        model.addAttribute("statuses", OrderStatus.values());
        return "admin/order/list";
    }

    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable Long id,
                               @RequestParam OrderStatus status,
                               RedirectAttributes redirectAttributes) {
        try {
            orderService.updateOrderStatus(id, status);
            redirectAttributes.addFlashAttribute("success", "Sifariş statusu yeniləndi.");
        } catch (ResourceNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", "Sifariş tapılmadı: #" + id);
        }
        return "redirect:/dashboard/order";
    }
}
