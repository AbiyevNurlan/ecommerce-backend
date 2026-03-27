package az.edu.itbrains.ecommerce.services;

import az.edu.itbrains.ecommerce.dtos.seller.PromotionCreateDto;
import az.edu.itbrains.ecommerce.dtos.seller.PromotionDto;
import az.edu.itbrains.ecommerce.models.Product;

import java.util.List;

public interface PromotionService {

    /** Satıcı promosyon alır — balansdan düşür */
    PromotionDto buyPromotion(String email, PromotionCreateDto dto);

    /** Satıcının bütün promosyonları */
    List<PromotionDto> getSellerPromotions(String email);

    /** Satıcı aktiv promosyonu ləğv edir (PENDING/ACTIVE → CANCELLED, pul geri verilmir) */
    void cancelPromotion(String email, Long promotionId);

    /** Homepage üçün: admin-featured + promotion-featured məhsullar */
    List<Product> getFeaturedProducts();

    /** Homepage üçün: admin-hotTrending + promotion-hotTrending məhsullar */
    List<Product> getHotTrendingProducts();

    /** Scheduler çağırır — bitmüş promosyonları EXPIRED edir */
    void expireOutdatedPromotions();

    /** Promosyon tipi + gün sayı üçün qiymət hesablama */
    java.math.BigDecimal calculatePrice(az.edu.itbrains.ecommerce.enums.PromotionType type, int days);
}
