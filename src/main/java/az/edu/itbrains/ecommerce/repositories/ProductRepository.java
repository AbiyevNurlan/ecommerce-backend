package az.edu.itbrains.ecommerce.repositories;

import az.edu.itbrains.ecommerce.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {


    List<Product> findTop8ByOrderByIdDesc();
    List<Product> findTop3ByFeaturedTrueOrderByIdDesc();
    List<Product> findTop3ByHotTrendingTrueOrderByIdDesc();

}
