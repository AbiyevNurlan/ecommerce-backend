package az.edu.itbrains.ecommerce.dtos.colorSize;

import az.edu.itbrains.ecommerce.dtos.color.ColorProductCreateDto;
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
