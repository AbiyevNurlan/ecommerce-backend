package az.edu.itbrains.ecommerce.dtos.color;


import az.edu.itbrains.ecommerce.dtos.size.SizeDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ColorDto {
    private Long id;
    private String name;
}
