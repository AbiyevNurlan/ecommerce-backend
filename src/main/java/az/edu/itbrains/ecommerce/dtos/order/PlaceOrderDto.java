package az.edu.itbrains.ecommerce.dtos.order;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlaceOrderDto {

    @NotBlank(message = "Ünvan mütləq doldurulmalıdır")
    private String address;
}
