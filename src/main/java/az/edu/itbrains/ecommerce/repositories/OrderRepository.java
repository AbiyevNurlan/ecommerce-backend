package az.edu.itbrains.ecommerce.repositories;

import az.edu.itbrains.ecommerce.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.orderItems oi LEFT JOIN FETCH oi.product WHERE o.user.email = :email ORDER BY o.id DESC")
    List<Order> findByUserEmailOrderByIdDesc(@Param("email") String email);

    @Query("SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.orderItems oi LEFT JOIN FETCH oi.product LEFT JOIN FETCH o.user ORDER BY o.id DESC")
    List<Order> findAllByOrderByIdDesc();
}
