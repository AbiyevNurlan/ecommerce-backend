package az.edu.itbrains.ecommerce.services.impls;

import az.edu.itbrains.ecommerce.enums.ProductStatus;
import az.edu.itbrains.ecommerce.dtos.product.*;
import az.edu.itbrains.ecommerce.exceptions.ResourceNotFoundException;
import az.edu.itbrains.ecommerce.exceptions.ServiceException;
import az.edu.itbrains.ecommerce.models.Category;
import az.edu.itbrains.ecommerce.models.Photo;
import az.edu.itbrains.ecommerce.models.Product;
import az.edu.itbrains.ecommerce.models.Seller;
import az.edu.itbrains.ecommerce.repositories.OrderItemRepository;
import az.edu.itbrains.ecommerce.repositories.ProductRepository;
import az.edu.itbrains.ecommerce.repositories.SellerRepository;
import az.edu.itbrains.ecommerce.services.CategoryService;
import az.edu.itbrains.ecommerce.services.ColorSizeService;
import az.edu.itbrains.ecommerce.services.ProductService;
import az.edu.itbrains.ecommerce.services.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final CategoryService categoryService;
    private final ColorSizeService colorSizeService;
    private final OrderItemRepository orderItemRepository;
    private final SellerRepository sellerRepository;
    private final RecommendationService recommendationService;

    @Override
    @Transactional(readOnly = true)
    public List<ProductHomeDto> getAllHomeProducts() {
        return productRepository.findTop8ByOrderByIdDesc()
                .stream()
                .map(product -> {
                    ProductHomeDto dto = modelMapper.map(product, ProductHomeDto.class);
                    dto.setImage(getSelectedPhotoUrl(product));
                    return dto;
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDetailDto getProductDetail(Long id) {
        Objects.requireNonNull(id, "Product id must not be null");
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id, "Product"));
        return modelMapper.map(product, ProductDetailDto.class);
    }

    @Override
    @Transactional
    public void createProduct(ProductCreateDto productCreateDto) {
        Product product = new Product();
        product.setName(productCreateDto.getName());
        product.setDescription(productCreateDto.getDescription());
        product.setShortDescription(productCreateDto.getShortDescription());
        product.setSpecification("");
        product.setPrice(productCreateDto.getPrice());
        product.setDiscount(productCreateDto.getDiscount());
        product.setBarcode(productCreateDto.getBarcode());
        Category category = categoryService.getCategoryById(productCreateDto.getCategoryId());
        product.setCategory(category);
        productRepository.save(product);
        colorSizeService.createColorSize(productCreateDto.getColorSizes(), product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDashboardDto> getDashboardProducts() {
        return productRepository.findAll()
                .stream()
                .map(product -> {
                    ProductDashboardDto dto = modelMapper.map(product, ProductDashboardDto.class);
                    dto.setImage(getSelectedPhotoUrl(product));
                    return dto;
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductFeaturedDto> getFeaturedProducts() {
        return productRepository.findTop3ByFeaturedTrueOrderByIdDesc()
                .stream()
                .map(product -> {
                    ProductFeaturedDto dto = modelMapper.map(product, ProductFeaturedDto.class);
                    dto.setImage(getSelectedPhotoUrl(product));
                    return dto;
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductHotTrendDto> getHotTrendProducts() {
        return productRepository.findTop3ByHotTrendingTrueOrderByIdDesc()
                .stream()
                .map(product -> {
                    ProductHotTrendDto dto = modelMapper.map(product, ProductHotTrendDto.class);
                    dto.setImage(getSelectedPhotoUrl(product));
                    return dto;
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductBestSellerDto> getBestSellerProducts() {
        List<Product> products = orderItemRepository.findBestSellerProducts(PageRequest.of(0, 4));
        if (products.isEmpty()) {
            // Fallback when no orders exist yet: show 4 most recent products
            products = productRepository.findTop8ByOrderByIdDesc().stream().limit(4).toList();
        }
        return products.stream()
                .map(product -> {
                    ProductBestSellerDto dto = modelMapper.map(product, ProductBestSellerDto.class);
                    dto.setImage(getSelectedPhotoUrl(product));
                    return dto;
                })
                .toList();
    }

    private String getSelectedPhotoUrl(Product product) {
        return product.getPhotos().stream()
                .filter(Photo::isSelected)
                .findFirst()
                .map(Photo::getUrl)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Product getProductById(Long productId) {
        Objects.requireNonNull(productId, "Product id must not be null");
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(productId, "Product"));
    }

    @Override
    @Transactional
    public boolean deleteProduct(Long id) {
        Objects.requireNonNull(id, "Product id must not be null");
        productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id, "Product"));
        try {
            productRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            log.error("Failed to delete product with id: {}", id, e);
            throw new ServiceException("Failed to delete product with id: " + id, e);
        }
    }

    @Override
    @Transactional
    public void updateProduct(Long id, ProductUpdateDto dto) {
        Objects.requireNonNull(id, "Product id must not be null");
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id, "Product"));
        if (dto.getName() != null && !dto.getName().isBlank()) product.setName(dto.getName());
        if (dto.getDescription() != null) product.setDescription(dto.getDescription());
        if (dto.getShortDescription() != null) product.setShortDescription(dto.getShortDescription());
        if (dto.getSpecification() != null) product.setSpecification(dto.getSpecification());
        product.setPrice(dto.getPrice());
        product.setDiscount(dto.getDiscount());
        product.setFeatured(dto.isFeatured());
        product.setHotTrending(dto.isHotTrending());
        if (dto.getBarcode() != null && !dto.getBarcode().isBlank()) product.setBarcode(dto.getBarcode());
        if (dto.getCategoryId() != null) {
            Category category = categoryService.getCategoryById(dto.getCategoryId());
            product.setCategory(category);
        }
        productRepository.save(product);
        log.info("Product #{} updated", id);
    }

    @Override
    @Transactional
    public void createProductForSeller(String sellerEmail, ProductCreateDto dto) {
        Seller seller = sellerRepository.findByUserEmail(sellerEmail)
                .orElseThrow(() -> new ServiceException("Satıcı profili tapılmadı"));
        if (!seller.isApproved()) {
            throw new ServiceException("Hesabınız hələ təsdiqlənməmişdir");
        }
        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setShortDescription(dto.getShortDescription());
        product.setSpecification("");
        product.setPrice(dto.getPrice());
        product.setDiscount(dto.getDiscount());
        product.setBarcode(dto.getBarcode());
        product.setProductStatus(ProductStatus.PENDING_REVIEW);
        product.setSeller(seller);
        Category category = categoryService.getCategoryById(dto.getCategoryId());
        product.setCategory(category);
        productRepository.save(product);
        colorSizeService.createColorSize(dto.getColorSizes(), product);
        log.info("Seller product created — seller={}, product={}", sellerEmail, product.getName());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDashboardDto> getSellerProducts(String sellerEmail) {
        Seller seller = sellerRepository.findByUserEmail(sellerEmail)
                .orElseThrow(() -> new ServiceException("Satıcı profili tapılmadı"));
        return productRepository.findBySellerIdOrderByIdDesc(seller.getId())
                .stream()
                .map(product -> {
                    ProductDashboardDto dashDto = modelMapper.map(product, ProductDashboardDto.class);
                    dashDto.setImage(getSelectedPhotoUrl(product));
                    return dashDto;
                })
                .toList();
    }

    @Override
    @Transactional
    public boolean deleteSellerProduct(String sellerEmail, Long productId) {
        Objects.requireNonNull(productId);
        Seller seller = sellerRepository.findByUserEmail(sellerEmail)
                .orElseThrow(() -> new ServiceException("Satıcı profili tapılmadı"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(productId, "Product"));
        if (product.getSeller() == null || !product.getSeller().getId().equals(seller.getId())) {
            throw new ServiceException("Bu məhsul sizə aid deyil");
        }
        productRepository.deleteById(productId);
        return true;
    }

    @Override
    @Transactional
    public void updateSellerProduct(String sellerEmail, Long productId, ProductUpdateDto dto) {
        Objects.requireNonNull(productId);
        Seller seller = sellerRepository.findByUserEmail(sellerEmail)
                .orElseThrow(() -> new ServiceException("Satıcı profili tapılmadı"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(productId, "Product"));
        if (product.getSeller() == null || !product.getSeller().getId().equals(seller.getId())) {
            throw new ServiceException("Bu məhsul sizə aid deyil");
        }
        if (dto.getName() != null && !dto.getName().isBlank()) product.setName(dto.getName());
        if (dto.getDescription() != null) product.setDescription(dto.getDescription());
        if (dto.getShortDescription() != null) product.setShortDescription(dto.getShortDescription());
        product.setPrice(dto.getPrice());
        product.setDiscount(dto.getDiscount());
        if (dto.getBarcode() != null && !dto.getBarcode().isBlank()) product.setBarcode(dto.getBarcode());
        if (dto.getCategoryId() != null) {
            product.setCategory(categoryService.getCategoryById(dto.getCategoryId()));
        }
        // Satıcı featured/hotTrending-i birbaşa dəyişə bilməz — yalnız promosyon ilə
        productRepository.save(product);
        log.info("Seller product #{} updated by {}", productId, sellerEmail);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDashboardDto> searchProducts(String query) {
        List<Product> raw = productRepository.searchProducts(query);
        // Relevance sort: name match first, then category, then description
        String lower = query.toLowerCase();
        List<Product> sorted = new ArrayList<>(raw);
        sorted.sort((a, b) -> Integer.compare(relevanceScore(a, lower), relevanceScore(b, lower)));
        return sorted.stream()
                .map(product -> {
                    ProductDashboardDto dto = modelMapper.map(product, ProductDashboardDto.class);
                    dto.setImage(getSelectedPhotoUrl(product));
                    return dto;
                })
                .toList();
    }

    private int relevanceScore(Product p, String lowerQuery) {
        if (p.getName() != null && p.getName().toLowerCase().contains(lowerQuery)) return 0;
        if (p.getCategory() != null && p.getCategory().getName() != null
                && p.getCategory().getName().toLowerCase().contains(lowerQuery)) return 1;
        return 2;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductFeaturedDto> getRecommendations(Long productId) {
        return recommendationService.getFrequentlyBoughtTogether(productId)
                .stream()
                .map(product -> {
                    ProductFeaturedDto dto = modelMapper.map(product, ProductFeaturedDto.class);
                    dto.setImage(getSelectedPhotoUrl(product));
                    return dto;
                })
                .toList();
    }
}
