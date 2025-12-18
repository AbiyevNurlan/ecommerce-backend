package az.edu.itbrains.ecommerce.dtos.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductBasketDto {
    private Long id;
    private String name;
    private Double price;
    private Double discount;
    private String imageUrl;
}
