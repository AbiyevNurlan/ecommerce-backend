package az.edu.itbrains.ecommerce.dtos.seller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SellerDashboardDto {
    private String shopName;
    private BigDecimal balance;
    private BigDecimal commissionRate;
    private boolean approved;
    private int totalProducts;
    private int activePromotions;
    private long totalOrders;
    private BigDecimal totalRevenue;
}
