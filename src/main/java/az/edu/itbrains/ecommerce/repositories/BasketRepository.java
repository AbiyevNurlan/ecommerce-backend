package az.edu.itbrains.ecommerce.repositories;

import az.edu.itbrains.ecommerce.models.Basket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BasketRepository extends JpaRepository<Basket, Long> {

    Basket findByUserIdAndProductId(Long id, Long id1);

    List<Basket> findByUserId(Long userId);

    @Query("SELECT b FROM Basket b JOIN FETCH b.product WHERE b.user.id = :userId")
    List<Basket> findByUserIdWithProduct(@Param("userId") Long userId);

    @Modifying
    @Query("DELETE FROM Basket b WHERE b.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);
}
