package az.edu.itbrains.ecommerce.repositories;

import az.edu.itbrains.ecommerce.models.OrderItem;
import az.edu.itbrains.ecommerce.models.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("SELECT oi.product FROM OrderItem oi GROUP BY oi.product ORDER BY SUM(oi.quantity) DESC")
    List<Product> findBestSellerProducts(Pageable pageable);

    @Query("SELECT COUNT(DISTINCT oi.order.id) FROM OrderItem oi WHERE oi.product.id IN :productIds")
    long countDistinctOrdersBySellerProducts(List<Long> productIds);

    @Query("SELECT COALESCE(SUM(oi.price * oi.quantity), 0) FROM OrderItem oi WHERE oi.product.id IN :productIds")
    BigDecimal sumRevenueByProductIds(List<Long> productIds);

    /**
     * Collaborative Filtering (Item-Based).
     *
     * Finds product IDs that co-appear most frequently with the given product
     * across all orders.  The join on order_id surfaces every pair of products
     * bought together; GROUP BY + COUNT(*) ranks them by co-purchase frequency.
     *
     * SQL (native PostgreSQL):
     *   SELECT oi2.product_id
     *   FROM order_item oi1
     *   INNER JOIN order_item oi2 ON oi1.order_id = oi2.order_id
     *   WHERE oi1.product_id = :productId AND oi2.product_id <> :productId
     *   GROUP BY oi2.product_id
     *   ORDER BY COUNT(*) DESC
     *   LIMIT 4
     */
    @Query(value = """
            SELECT oi2.product_id
            FROM order_item oi1
            INNER JOIN order_item oi2 ON oi1.order_id = oi2.order_id
            WHERE oi1.product_id = :productId
              AND oi2.product_id <> :productId
            GROUP BY oi2.product_id
            ORDER BY COUNT(*) DESC
            LIMIT 4
            """, nativeQuery = true)
    List<Long> findFrequentlyBoughtTogetherIds(@Param("productId") Long productId);
}

