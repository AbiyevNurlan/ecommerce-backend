package az.edu.itbrains.ecommerce.repositories;

import az.edu.itbrains.ecommerce.models.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SellerRepository extends JpaRepository<Seller, Long> {
    Optional<Seller> findByUserEmail(String email);
    Optional<Seller> findByUserId(Long userId);
    boolean existsByShopName(String shopName);
    boolean existsByUserId(Long userId);
}
