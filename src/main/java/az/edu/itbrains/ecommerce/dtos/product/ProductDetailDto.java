package az.edu.itbrains.ecommerce.dtos.product;


import az.edu.itbrains.ecommerce.dtos.category.CategoryDto;
import az.edu.itbrains.ecommerce.dtos.color.ColorDto;
import az.edu.itbrains.ecommerce.dtos.photo.PhotoDto;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailDto {
    private Long id;
    private String name;
    private String description;
    private String shortDescription;
    private String specification;
    private double price;
    private double discount;
    private String barcode;

    private CategoryDto category;
    private List<ColorDto> colors;
    private List<PhotoDto> photos;
}
