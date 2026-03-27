package az.edu.itbrains.ecommerce.dtos.seller;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/** Admin-in satıcı balansına kredit əlavə etmək üçün DTO */
@Getter
@Setter
public class BalanceCreditDto {

    @NotNull
    private Long sellerId;

    @NotNull
    @DecimalMin(value = "1.00", message = "Minimum 1.00 ₼ əlavə etmək olar")
    private BigDecimal amount;

    private String description;
}
