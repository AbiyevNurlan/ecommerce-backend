package az.edu.itbrains.ecommerce.repositories;

import az.edu.itbrains.ecommerce.models.Basket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasketRepository extends JpaRepository<Basket, Long> {

    Basket findByUserIdAndProductId(Long id, Long id1);
}
