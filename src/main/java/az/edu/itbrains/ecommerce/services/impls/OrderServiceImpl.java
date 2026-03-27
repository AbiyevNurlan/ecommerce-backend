package az.edu.itbrains.ecommerce.services.impls;

import az.edu.itbrains.ecommerce.dtos.order.AdminOrderDto;
import az.edu.itbrains.ecommerce.dtos.order.OrderDto;
import az.edu.itbrains.ecommerce.dtos.order.OrderItemDto;
import az.edu.itbrains.ecommerce.dtos.order.PlaceOrderDto;
import az.edu.itbrains.ecommerce.enums.OrderStatus;
import az.edu.itbrains.ecommerce.exceptions.ResourceNotFoundException;
import az.edu.itbrains.ecommerce.exceptions.ServiceException;
import az.edu.itbrains.ecommerce.models.Basket;
import az.edu.itbrains.ecommerce.models.Order;
import az.edu.itbrains.ecommerce.models.OrderItem;
import az.edu.itbrains.ecommerce.models.User;
import az.edu.itbrains.ecommerce.repositories.BasketRepository;
import az.edu.itbrains.ecommerce.repositories.OrderItemRepository;
import az.edu.itbrains.ecommerce.repositories.OrderRepository;
import az.edu.itbrains.ecommerce.services.OrderService;
import az.edu.itbrains.ecommerce.services.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final BasketRepository basketRepository;
    private final UserService userService;

    @Override
    @Transactional
    public OrderDto placeOrder(String email, PlaceOrderDto placeOrderDto) {
        User user = userService.getByEmail(email);
        if (user == null) {
            throw new ServiceException("User not found: " + email);
        }

        List<Basket> baskets = basketRepository.findByUserId(user.getId());
        if (baskets.isEmpty()) {
            throw new ServiceException("Səbət boşdur. Zəhmət olmasa məhsul əlavə edin.");
        }

        Order order = new Order();
        order.setUser(user);
        order.setAddress(placeOrderDto.getAddress());
        order.setOrderStatus(OrderStatus.PENDING);
        Order savedOrder = orderRepository.save(order);

        List<OrderItemDto> itemDtos = baskets.stream().map(basket -> {
            OrderItem item = new OrderItem();
            item.setOrder(savedOrder);
            item.setProduct(basket.getProduct());
            item.setPrice(basket.getProduct().getPrice());
            item.setQuantity(basket.getQuantity());
            orderItemRepository.save(item);

            OrderItemDto dto = new OrderItemDto();
            dto.setProductName(basket.getProduct().getName());
            dto.setPrice(basket.getProduct().getPrice());
            dto.setQuantity(basket.getQuantity());
            return dto;
        }).toList();

        basketRepository.deleteByUserId(user.getId());

        log.info("Order #{} placed by user '{}'", savedOrder.getId(), email);

        OrderDto result = new OrderDto();
        result.setId(savedOrder.getId());
        result.setOrderStatus(OrderStatus.PENDING.name());
        result.setAddress(savedOrder.getAddress());
        result.setOrderItems(itemDtos);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getUserOrders(String email) {
        return orderRepository.findByUserEmailOrderByIdDesc(email)
                .stream()
                .map(this::toOrderDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminOrderDto> getAllOrders() {
        return orderRepository.findAllByOrderByIdDesc()
                .stream()
                .map(this::toAdminOrderDto)
                .toList();
    }

    @Override
    @Transactional
    @SuppressWarnings("null")
    public AdminOrderDto updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(orderId, "Order"));
        order.setOrderStatus(status);
        Order saved = orderRepository.save(order);
        log.info("Order #{} status updated to {}", orderId, status);
        return toAdminOrderDto(saved);
    }

    // ─── Private mapping helpers ──────────────────────────────────────────────

    private OrderDto toOrderDto(Order order) {
        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setOrderStatus(order.getOrderStatus().name());
        dto.setAddress(order.getAddress());
        dto.setOrderItems(order.getOrderItems().stream().map(this::toItemDto).toList());
        return dto;
    }

    private AdminOrderDto toAdminOrderDto(Order order) {
        AdminOrderDto dto = new AdminOrderDto();
        dto.setId(order.getId());
        dto.setOrderStatus(order.getOrderStatus().name());
        dto.setAddress(order.getAddress());
        dto.setUserEmail(order.getUser().getEmail());
        dto.setUserName(order.getUser().getName() + " " + order.getUser().getSurname());
        dto.setOrderItems(order.getOrderItems().stream().map(this::toItemDto).toList());
        return dto;
    }

    private OrderItemDto toItemDto(OrderItem item) {
        OrderItemDto dto = new OrderItemDto();
        dto.setProductName(item.getProduct().getName());
        dto.setPrice(item.getPrice());
        dto.setQuantity(item.getQuantity());
        return dto;
    }
}
