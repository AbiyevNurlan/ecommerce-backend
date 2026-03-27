package az.edu.itbrains.ecommerce.repositories;

import az.edu.itbrains.ecommerce.models.SellerTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SellerTransactionRepository extends JpaRepository<SellerTransaction, Long> {
    List<SellerTransaction> findBySellerIdOrderByCreatedAtDesc(Long sellerId);
}
