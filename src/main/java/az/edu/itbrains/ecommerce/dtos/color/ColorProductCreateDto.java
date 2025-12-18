package az.edu.itbrains.ecommerce.dtos.color;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ColorProductCreateDto {
    private Long sizeId;
    private Integer quantity;
}
