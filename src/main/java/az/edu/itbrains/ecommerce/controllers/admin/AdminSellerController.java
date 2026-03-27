package az.edu.itbrains.ecommerce.controllers.admin;

import az.edu.itbrains.ecommerce.dtos.seller.BalanceCreditDto;
import az.edu.itbrains.ecommerce.services.SellerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/dashboard/sellers")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminSellerController {

    private final SellerService sellerService;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("sellers", sellerService.getAllSellers());
        model.addAttribute("creditDto", new BalanceCreditDto());
        return "admin/seller/index";
    }

    @PostMapping("/approve/{id}")
    public String approve(@PathVariable Long id, RedirectAttributes ra) {
        try {
            sellerService.approveSeller(id);
            ra.addFlashAttribute("success", "Satıcı təsdiqləndi.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/dashboard/sellers";
    }

    @PostMapping("/reject/{id}")
    public String reject(@PathVariable Long id, RedirectAttributes ra) {
        try {
            sellerService.rejectSeller(id);
            ra.addFlashAttribute("success", "Satıcı rədd edildi.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/dashboard/sellers";
    }

    @PostMapping("/credit")
    public String creditBalance(@Valid @ModelAttribute BalanceCreditDto dto,
                                RedirectAttributes ra) {
        try {
            sellerService.creditBalance(dto);
            ra.addFlashAttribute("success",
                    "Satıcı balansına " + dto.getAmount() + "₼ əlavə edildi.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/dashboard/sellers";
    }
}
