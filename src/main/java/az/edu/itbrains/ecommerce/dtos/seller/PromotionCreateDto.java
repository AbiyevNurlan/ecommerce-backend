package az.edu.itbrains.ecommerce.dtos.seller;

import az.edu.itbrains.ecommerce.enums.PromotionType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PromotionCreateDto {

    @NotNull(message = "Məhsul seçilməlidir")
    private Long productId;

    @NotNull(message = "Promosyon tipi seçilməlidir")
    private PromotionType promotionType;

    /**
     * Gün sayı: 3, 7 və ya 30
     * Qiymət avtomatik hesablanır:
     *   FEATURED    : 3g=10₼  7g=20₼  30g=60₼
     *   SPONSORED   : 3g=5₼   7g=10₼  30g=30₼
     *   HOT_TRENDING: 3g=7₼   7g=15₼  30g=45₼
     */
    @NotNull(message = "Müddət seçilməlidir")
    @Positive
    private Integer durationDays;
}
