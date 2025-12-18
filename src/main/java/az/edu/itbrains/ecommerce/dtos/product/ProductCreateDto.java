package az.edu.itbrains.ecommerce.dtos.product;


import az.edu.itbrains.ecommerce.dtos.color.ColorDto;
import az.edu.itbrains.ecommerce.dtos.colorSize.ColorSizeCreateDto;
import az.edu.itbrains.ecommerce.dtos.photo.PhotoDto;
import az.edu.itbrains.ecommerce.models.Category;
import az.edu.itbrains.ecommerce.models.Color;
import az.edu.itbrains.ecommerce.models.Photo;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductCreateDto {


    private String name;
    private String description;
    private String shortDescription;
    private String specification;
    private Double price;
    private Double discount;
    private String barcode;

    private Long categoryId;

    private List<ColorDto> colors = new ArrayList<>();

    private List<PhotoDto> photos = new ArrayList<>();

    private List<ColorSizeCreateDto> colorSizes = new ArrayList<>();
}
