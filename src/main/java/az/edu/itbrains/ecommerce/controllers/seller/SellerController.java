package az.edu.itbrains.ecommerce.controllers.seller;

import az.edu.itbrains.ecommerce.dtos.seller.PromotionCreateDto;
import az.edu.itbrains.ecommerce.dtos.seller.SellerApplyDto;
import az.edu.itbrains.ecommerce.dtos.seller.SellerDashboardDto;
import az.edu.itbrains.ecommerce.dtos.seller.SellerTransactionDto;
import az.edu.itbrains.ecommerce.enums.PromotionType;
import az.edu.itbrains.ecommerce.models.Seller;
import az.edu.itbrains.ecommerce.repositories.SellerTransactionRepository;
import az.edu.itbrains.ecommerce.services.CategoryService;
import az.edu.itbrains.ecommerce.services.ProductService;
import az.edu.itbrains.ecommerce.services.PromotionService;
import az.edu.itbrains.ecommerce.services.SellerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/seller")
@RequiredArgsConstructor
public class SellerController {

    private final SellerService sellerService;
    private final ProductService productService;
    private final PromotionService promotionService;
    private final CategoryService categoryService;
    private final SellerTransactionRepository transactionRepository;

    // ─── Apply ───────────────────────────────────────────────────────────────

    @GetMapping("/apply")
    public String applyPage(Model model, Principal principal) {
        Seller existing = sellerService.findByEmail(principal.getName());
        if (existing != null) {
            return "redirect:/seller/dashboard";
        }
        model.addAttribute("sellerApplyDto", new SellerApplyDto());
        return "seller/apply";
    }

    @PostMapping("/apply")
    public String applySubmit(@Valid @ModelAttribute SellerApplyDto dto,
                              BindingResult result,
                              Principal principal,
                              RedirectAttributes ra) {
        if (result.hasErrors()) {
            return "seller/apply";
        }
        try {
            sellerService.applyForSeller(principal.getName(), dto);
            ra.addFlashAttribute("success", "Müraciətiniz qəbul edildi. Admin təsdiqini gözləyin.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/seller/apply";
    }

    // ─── Dashboard ────────────────────────────────────────────────────────────

    @GetMapping({"/dashboard", ""})
    @PreAuthorize("hasRole('SELLER')")
    public String dashboard(Model model, Principal principal) {
        SellerDashboardDto stats = sellerService.getDashboardStats(principal.getName());
        model.addAttribute("stats", stats);
        return "seller/dashboard";
    }

    // ─── Products ─────────────────────────────────────────────────────────────

    @GetMapping("/products")
    @PreAuthorize("hasRole('SELLER')")
    public String products(Model model, Principal principal) {
        model.addAttribute("products", productService.getSellerProducts(principal.getName()));
        return "seller/products";
    }

    @GetMapping("/products/create")
    @PreAuthorize("hasRole('SELLER')")
    public String createProductPage(Model model) {
        model.addAttribute("productCreateDto",
                new az.edu.itbrains.ecommerce.dtos.product.ProductCreateDto());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "seller/product-create";
    }

    @PostMapping("/products/create")
    @PreAuthorize("hasRole('SELLER')")
    public String createProduct(
            @Valid @ModelAttribute("productCreateDto")
            az.edu.itbrains.ecommerce.dtos.product.ProductCreateDto dto,
            BindingResult result,
            Principal principal,
            Model model,
            RedirectAttributes ra) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            return "seller/product-create";
        }
        try {
            productService.createProductForSeller(principal.getName(), dto);
            ra.addFlashAttribute("success", "Məhsul əlavə edildi. Admin təsdiqini gözləyin.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/seller/products";
    }

    @PostMapping("/products/delete/{id}")
    @PreAuthorize("hasRole('SELLER')")
    public String deleteProduct(@PathVariable Long id,
                                Principal principal,
                                RedirectAttributes ra) {
        try {
            productService.deleteSellerProduct(principal.getName(), id);
            ra.addFlashAttribute("success", "Məhsul silindi.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/seller/products";
    }

    // ─── Promotions ───────────────────────────────────────────────────────────

    @GetMapping("/promotions")
    @PreAuthorize("hasRole('SELLER')")
    public String promotions(Model model, Principal principal) {
        model.addAttribute("promotions", promotionService.getSellerPromotions(principal.getName()));
        model.addAttribute("products", productService.getSellerProducts(principal.getName()));
        model.addAttribute("promotionTypes", PromotionType.values());
        model.addAttribute("dto", new PromotionCreateDto());

        // Qiymət cədvəlini view-a göndər
        model.addAttribute("priceTable", buildPriceTable());
        return "seller/promotions";
    }

    @PostMapping("/promotions/buy")
    @PreAuthorize("hasRole('SELLER')")
    public String buyPromotion(@Valid @ModelAttribute("dto") PromotionCreateDto dto,
                               BindingResult result,
                               Principal principal,
                               Model model,
                               RedirectAttributes ra) {
        if (result.hasErrors()) {
            return "redirect:/seller/promotions";
        }
        try {
            promotionService.buyPromotion(principal.getName(), dto);
            ra.addFlashAttribute("success", "Promosyon uğurla aktivləşdirildi!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/seller/promotions";
    }

    @PostMapping("/promotions/cancel/{id}")
    @PreAuthorize("hasRole('SELLER')")
    public String cancelPromotion(@PathVariable Long id,
                                  Principal principal,
                                  RedirectAttributes ra) {
        try {
            promotionService.cancelPromotion(principal.getName(), id);
            ra.addFlashAttribute("success", "Promosyon ləğv edildi.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/seller/promotions";
    }

    // ─── Balance & Transactions ────────────────────────────────────────────────

    @GetMapping("/balance")
    @PreAuthorize("hasRole('SELLER')")
    public String balance(Model model, Principal principal) {
        Seller seller = sellerService.findByEmail(principal.getName());
        List<SellerTransactionDto> txList = transactionRepository
                .findBySellerIdOrderByCreatedAtDesc(seller.getId())
                .stream()
                .map(t -> new SellerTransactionDto(
                        t.getId(), t.getAmount(), t.getTransactionType(),
                        t.getDescription(), t.getCreatedAt()))
                .toList();
        model.addAttribute("balance", seller.getBalance());
        model.addAttribute("transactions", txList);
        return "seller/balance";
    }

    // ─── Private ─────────────────────────────────────────────────────────────

    private java.util.Map<String, java.util.Map<Integer, BigDecimal>> buildPriceTable() {
        return java.util.Map.of(
                "FEATURED",     java.util.Map.of(3, new BigDecimal("10.00"), 7, new BigDecimal("20.00"), 30, new BigDecimal("60.00")),
                "SPONSORED",    java.util.Map.of(3, new BigDecimal("5.00"),  7, new BigDecimal("10.00"), 30, new BigDecimal("30.00")),
                "HOT_TRENDING", java.util.Map.of(3, new BigDecimal("7.00"),  7, new BigDecimal("15.00"), 30, new BigDecimal("45.00"))
        );
    }
}
