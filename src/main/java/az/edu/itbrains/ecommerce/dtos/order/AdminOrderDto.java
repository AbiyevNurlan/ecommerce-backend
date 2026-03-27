package az.edu.itbrains.ecommerce.dtos.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminOrderDto {

    private Long id;
    private String orderStatus;
    private String address;
    private String userEmail;
    private String userName;
    private List<OrderItemDto> orderItems;

    public Double getTotalPrice() {
        if (orderItems == null) return 0.0;
        return orderItems.stream().mapToDouble(OrderItemDto::getItemTotal).sum();
    }
}
