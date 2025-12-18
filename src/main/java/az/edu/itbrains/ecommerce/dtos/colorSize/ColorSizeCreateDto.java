package az.edu.itbrains.ecommerce.dtos.colorSize;

import az.edu.itbrains.ecommerce.dtos.color.ColorProductCreateDto;
import az.edu.itbrains.ecommerce.models.Color;
import az.edu.itbrains.ecommerce.models.Product;
import az.edu.itbrains.ecommerce.models.Size;
import jakarta.persistence.ManyToOne;
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
public class ColorSizeCreateDto {
    private Long colorId;
    private List<ColorProductCreateDto> productSizes = new ArrayList<>();

}
