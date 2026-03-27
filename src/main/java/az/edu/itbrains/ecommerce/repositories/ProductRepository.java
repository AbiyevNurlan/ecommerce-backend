package az.edu.itbrains.ecommerce.repositories;

import az.edu.itbrains.ecommerce.enums.ProductStatus;
import az.edu.itbrains.ecommerce.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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
}

