package az.edu.itbrains.ecommerce.dtos.basket;


import az.edu.itbrains.ecommerce.dtos.product.ProductBasketDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BasketUserDto {
    private Long id;
    private int quantity;
    private Double totalPrice;
    private ProductBasketDto product;


    public Double getTotalPrice() {
        return product.getPrice() * quantity;
    }
}
