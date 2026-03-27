package az.edu.itbrains.ecommerce.dtos.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDto {

    private String productName;
    private Double price;
    private int quantity;

    public Double getItemTotal() {
        return price != null ? price * quantity : 0.0;
    }
}
