package az.edu.itbrains.ecommerce.services;

import az.edu.itbrains.ecommerce.dtos.product.*;
import az.edu.itbrains.ecommerce.models.Product;

import java.util.List;

public interface ProductService {

    List<ProductHomeDto> getAllHomeProducts();

    ProductDetailDto getProductDetail(Long id);

    /**
     * Full-text search across product name, description, and category.
     * Results are sorted by relevance: name match > category match > description match.
     */
    List<ProductDashboardDto> searchProducts(String query);

    /**
     * Collaborative filtering recommendations for the given product.
     * Returns up to 4 products ranked by co-purchase frequency.
     */
    List<ProductFeaturedDto> getRecommendations(Long productId);

    void createProduct(ProductCreateDto productCreateDto);

    /** Satıcının öz məhsulları (email-dən seller tapılır) */
    void createProductForSeller(String sellerEmail, ProductCreateDto dto);

    List<ProductDashboardDto> getDashboardProducts();

    /** Satıcının öz məhsullarının siyahısı */
    List<ProductDashboardDto> getSellerProducts(String sellerEmail);

    List<ProductFeaturedDto> getFeaturedProducts();

    List<ProductHotTrendDto> getHotTrendProducts();

    List<ProductBestSellerDto> getBestSellerProducts();

    Product getProductById(Long productId);

    boolean deleteProduct(Long id);

    /** Satıcı yalnız öz məhsulunu silə bilər */
    boolean deleteSellerProduct(String sellerEmail, Long productId);

    void updateProduct(Long id, ProductUpdateDto productUpdateDto);

    /** Satıcı yalnız öz məhsulunu yeniləyə bilər */
    void updateSellerProduct(String sellerEmail, Long productId, ProductUpdateDto dto);
}

