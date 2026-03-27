package az.edu.itbrains.ecommerce.repositories;

import az.edu.itbrains.ecommerce.enums.PromotionStatus;
import az.edu.itbrains.ecommerce.enums.PromotionType;
import az.edu.itbrains.ecommerce.models.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface PromotionRepository extends JpaRepository<Promotion, Long> {

    List<Promotion> findBySellerIdOrderByCreatedAtDesc(Long sellerId);

    /** Hal-hazırda aktiv olan FEATURED tipli promotions-a aid məhsul id-lər */
    @Query("SELECT p.product.id FROM Promotion p WHERE p.promotionType = :type AND p.status = 'ACTIVE' AND p.endDate > :now")
    List<Long> findActiveProductIdsByType(PromotionType type, LocalDateTime now);

    /** end_date keçmiş amma hələ ACTIVE olan promosyonlar — scheduler üçün */
    List<Promotion> findByStatusAndEndDateBefore(PromotionStatus status, LocalDateTime now);

    /** Bir məhsul + satıcının hal-hazırda aktiv eyni tipli promosyonu varmı? */
    boolean existsByProductIdAndSellerIdAndPromotionTypeAndStatusIn(
            Long productId, Long sellerId, PromotionType type, List<PromotionStatus> statuses);
}
