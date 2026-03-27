package az.edu.itbrains.ecommerce.dtos.seller;

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
public class SellerAdminDto {
    private Long id;
    private Long userId;
    private String userEmail;
    private String userName;
    private String shopName;
    private String shopDescription;
    private BigDecimal balance;
    private BigDecimal commissionRate;
    private boolean approved;
    private int productCount;
    private LocalDateTime createdAt;
}
