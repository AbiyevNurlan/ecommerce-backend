package az.edu.itbrains.ecommerce.dtos.basket;

import az.edu.itbrains.ecommerce.dtos.product.ProductBasketDto;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BasketUserDto {
    private Long id;
    private int quantity;
    private ProductBasketDto product;

    public Double getTotalPrice() {
        if (product == null || product.getPrice() == null) {
            return 0.0;
        }
        return product.getPrice() * quantity;
    }
}