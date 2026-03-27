package az.edu.itbrains.ecommerce.services;

import az.edu.itbrains.ecommerce.dtos.order.AdminOrderDto;
import az.edu.itbrains.ecommerce.dtos.order.OrderDto;
import az.edu.itbrains.ecommerce.dtos.order.PlaceOrderDto;
import az.edu.itbrains.ecommerce.enums.OrderStatus;

import java.util.List;

public interface OrderService {

    OrderDto placeOrder(String email, PlaceOrderDto placeOrderDto);

    List<OrderDto> getUserOrders(String email);

    List<AdminOrderDto> getAllOrders();

    AdminOrderDto updateOrderStatus(Long orderId, OrderStatus status);
}
