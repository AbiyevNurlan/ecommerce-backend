package az.edu.itbrains.ecommerce.repositories;

import az.edu.itbrains.ecommerce.enums.ProductStatus;
import az.edu.itbrains.ecommerce.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findTop8ByOrderByIdDesc();
    List<Product> findTop3ByFeaturedTrueOrderByIdDesc();
    List<Product> findTop3ByHotTrendingTrueOrderByIdDesc();

    /** Seller'ın kendi ürünleri */
    List<Product> findBySellerIdOrderByIdDesc(Long sellerId);

    /** Sadece aktif durumda olan ürünler — storefront için */
    List<Product> findByProductStatus(ProductStatus status);

    /** Admin veya promotion tarafından featured olan ürünler */
    @Query("SELECT p FROM Product p WHERE p.featured = true OR p.id IN :promotedIds ORDER BY p.id DESC")
    List<Product> findFeaturedOrPromoted(List<Long> promotedIds);

    /** Admin veya promotion tarafından hot-trending olan ürünler */
    @Query("SELECT p FROM Product p WHERE p.hotTrending = true OR p.id IN :promotedIds ORDER BY p.id DESC")
    List<Product> findHotTrendingOrPromoted(List<Long> promotedIds);

    /**
     * Full-text relevance search across product name, description, and category.
     * Results are returned unordered from DB; relevance ranking is applied in the service layer.
     */
    @Query("SELECT DISTINCT p FROM Product p JOIN p.category c " +
           "WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :q, '%')) " +
           "OR LOWER(p.description) LIKE LOWER(CONCAT('%', :q, '%')) " +
           "OR LOWER(c.name) LIKE LOWER(CONCAT('%', :q, '%'))")
    List<Product> searchProducts(@Param("q") String q);

    /** Recommendation cold-start fallback: same category, excluding current product. */
    List<Product> findTop4ByCategoryIdAndIdNot(Long categoryId, Long id);

    /** Filter products by category SEO URL */
    List<Product> findByCategorySeoUrlOrderByIdDesc(String seoUrl);
}

