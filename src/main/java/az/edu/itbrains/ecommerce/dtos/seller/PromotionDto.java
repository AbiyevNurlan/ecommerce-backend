package az.edu.itbrains.ecommerce.dtos.seller;

import az.edu.itbrains.ecommerce.enums.PromotionStatus;
import az.edu.itbrains.ecommerce.enums.PromotionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PromotionDto {
    private Long id;
    private Long productId;
    private String productName;
    private PromotionType promotionType;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private BigDecimal amountPaid;
    private PromotionStatus status;
    private LocalDateTime createdAt;
}
