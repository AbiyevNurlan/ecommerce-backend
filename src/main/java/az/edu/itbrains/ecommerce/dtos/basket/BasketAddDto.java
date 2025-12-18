package az.edu.itbrains.ecommerce.dtos.basket;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BasketAddDto {
    private Long productId;
    private int quantity;
}
