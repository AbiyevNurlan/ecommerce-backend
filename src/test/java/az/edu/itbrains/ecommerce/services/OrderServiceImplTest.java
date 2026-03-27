package az.edu.itbrains.ecommerce.services;

import az.edu.itbrains.ecommerce.dtos.order.AdminOrderDto;
import az.edu.itbrains.ecommerce.dtos.order.OrderDto;
import az.edu.itbrains.ecommerce.dtos.order.PlaceOrderDto;
import az.edu.itbrains.ecommerce.enums.OrderStatus;
import az.edu.itbrains.ecommerce.exceptions.ResourceNotFoundException;
import az.edu.itbrains.ecommerce.exceptions.ServiceException;
import az.edu.itbrains.ecommerce.models.*;
import az.edu.itbrains.ecommerce.repositories.BasketRepository;
import az.edu.itbrains.ecommerce.repositories.OrderItemRepository;
import az.edu.itbrains.ecommerce.repositories.OrderRepository;
import az.edu.itbrains.ecommerce.services.impls.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("null")
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderItemRepository orderItemRepository;
    @Mock
    private BasketRepository basketRepository;
    @Mock
    private UserService userService;

    @InjectMocks
    private OrderServiceImpl orderService;

    private User user;
    private Product product;
    private Basket basket;
    private Order order;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("nurlan@test.com");
        user.setName("Nurlan");
        user.setSurname("Aliyev");

        product = new Product();
        product.setId(10L);
        product.setName("Laptop");
        product.setPrice(999.99);

        basket = new Basket();
        basket.setId(1L);
        basket.setUser(user);
        basket.setProduct(product);
        basket.setQuantity(2);

        order = new Order();
        order.setId(100L);
        order.setUser(user);
        order.setAddress("Baku, Azerbaijan");
        order.setOrderStatus(OrderStatus.PENDING);
    }

    // ─── placeOrder ───────────────────────────────────────────────────────────

    @Test
    @DisplayName("placeOrder: düzgün məlumatlarla sifariş verilir")
    void placeOrder_validData_createsOrderAndClearsBasket() {
        PlaceOrderDto dto = new PlaceOrderDto("Baku, Azerbaijan");

        when(userService.getByEmail("nurlan@test.com")).thenReturn(user);
        when(basketRepository.findByUserId(1L)).thenReturn(List.of(basket));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderItemRepository.save(any(OrderItem.class))).thenAnswer(inv -> inv.getArgument(0));

        OrderDto result = orderService.placeOrder("nurlan@test.com", dto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(100L);
        assertThat(result.getOrderStatus()).isEqualTo("PENDING");
        assertThat(result.getAddress()).isEqualTo("Baku, Azerbaijan");
        assertThat(result.getOrderItems()).hasSize(1);
        assertThat(result.getOrderItems().get(0).getProductName()).isEqualTo("Laptop");
        assertThat(result.getOrderItems().get(0).getQuantity()).isEqualTo(2);

        // Sifariş verildikdən sonra səbət təmizlənməlidir
        verify(basketRepository).deleteByUserId(1L);
    }

    @Test
    @DisplayName("placeOrder: istifadəçi tapılmadıqda ServiceException atır")
    void placeOrder_userNotFound_throwsServiceException() {
        when(userService.getByEmail("ghost@test.com")).thenReturn(null);

        assertThatThrownBy(() -> orderService.placeOrder("ghost@test.com", new PlaceOrderDto("Addr")))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("ghost@test.com");
    }

    @Test
    @DisplayName("placeOrder: səbət boş olduqda ServiceException atır")
    void placeOrder_emptyBasket_throwsServiceException() {
        when(userService.getByEmail("nurlan@test.com")).thenReturn(user);
        when(basketRepository.findByUserId(1L)).thenReturn(List.of());

        assertThatThrownBy(() -> orderService.placeOrder("nurlan@test.com", new PlaceOrderDto("Some Addr")))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("Səbət boşdur");

        verify(orderRepository, never()).save(any());
    }

    @Test
    @DisplayName("placeOrder: hər basket üçün OrderItem yaradılır")
    void placeOrder_multipleBasketItems_savesOrderItemsForEach() {
        Basket basket2 = new Basket();
        basket2.setId(2L);
        basket2.setUser(user);
        Product product2 = new Product();
        product2.setId(20L);
        product2.setName("Phone");
        product2.setPrice(500.0);
        basket2.setProduct(product2);
        basket2.setQuantity(1);

        PlaceOrderDto dto = new PlaceOrderDto("Baku");

        when(userService.getByEmail("nurlan@test.com")).thenReturn(user);
        when(basketRepository.findByUserId(1L)).thenReturn(List.of(basket, basket2));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderItemRepository.save(any(OrderItem.class))).thenAnswer(inv -> inv.getArgument(0));

        OrderDto result = orderService.placeOrder("nurlan@test.com", dto);

        assertThat(result.getOrderItems()).hasSize(2);
        verify(orderItemRepository, times(2)).save(any(OrderItem.class));
        verify(basketRepository).deleteByUserId(1L);
    }

    // ─── updateOrderStatus ────────────────────────────────────────────────────

    @Test
    @DisplayName("updateOrderStatus: mövcud sifariş statusu yenilənir")
    void updateOrderStatus_existingOrder_updatesStatus() {
        Order orderWithItems = new Order();
        orderWithItems.setId(100L);
        orderWithItems.setUser(user);
        orderWithItems.setAddress("Baku");
        orderWithItems.setOrderStatus(OrderStatus.PENDING);

        when(orderRepository.findById(100L)).thenReturn(Optional.of(orderWithItems));
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> {
            Order o = inv.getArgument(0);
            o.setOrderStatus(OrderStatus.CONFIRMED);
            return o;
        });

        AdminOrderDto result = orderService.updateOrderStatus(100L, OrderStatus.CONFIRMED);

        assertThat(result).isNotNull();
        assertThat(result.getOrderStatus()).isEqualTo("CONFIRMED");
        verify(orderRepository).save(orderWithItems);
    }

    @Test
    @DisplayName("updateOrderStatus: mövcud olmayan sifariş üçün ResourceNotFoundException atır")
    void updateOrderStatus_nonExistingOrder_throwsResourceNotFoundException() {
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.updateOrderStatus(999L, OrderStatus.CONFIRMED))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(orderRepository, never()).save(any());
    }

    // ─── getUserOrders ────────────────────────────────────────────────────────

    @Test
    @DisplayName("getUserOrders: istifadəçinin sifarişlərini qaytarır")
    void getUserOrders_returnsUserOrderList() {
        OrderItem item = new OrderItem();
        item.setProduct(product);
        item.setPrice(999.99);
        item.setQuantity(1);
        order.setOrderItems(List.of(item));

        when(orderRepository.findByUserEmailOrderByIdDesc("nurlan@test.com"))
                .thenReturn(List.of(order));

        List<OrderDto> result = orderService.getUserOrders("nurlan@test.com");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(100L);
        assertThat(result.get(0).getOrderItems()).hasSize(1);
    }
}
