package az.edu.itbrains.ecommerce.dtos.product;


import az.edu.itbrains.ecommerce.dtos.category.CategoryDto;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductHomeDto {
    private Long id;
    private String name;
    private String image;
    private double price;
    private double discount;
    private CategoryDto category;
}
