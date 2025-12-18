package az.edu.itbrains.ecommerce.services.impls;

import az.edu.itbrains.ecommerce.dtos.product.*;
import az.edu.itbrains.ecommerce.exceptions.ResourceNotFoundException;
import az.edu.itbrains.ecommerce.models.Category;
import az.edu.itbrains.ecommerce.models.Product;
import az.edu.itbrains.ecommerce.repositories.ProductRepository;
import az.edu.itbrains.ecommerce.services.CategoryService;
import az.edu.itbrains.ecommerce.services.ColorService;
import az.edu.itbrains.ecommerce.services.ColorSizeService;
import az.edu.itbrains.ecommerce.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final CategoryService categoryService;
    private final ColorSizeService colorSizeService;

    @Override
    public List<ProductHomeDto> getAllHomeProducts() {
        List<Product> products = productRepository.findTop8ByOrderByIdDesc();
        if (!products.isEmpty()) {
            List<ProductHomeDto> productHomeDtoList = products.stream()
                    .map(product -> modelMapper.map(product, ProductHomeDto.class)).toList();

            return productHomeDtoList;
        }
        return List.of();
    }

    @Override
    public ProductDetailDto getProductDetail(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id, "Product"));
        if (product != null) {
            return modelMapper.map(product, ProductDetailDto.class);
        }
        return null;
    }

    @Override
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

        boolean result = colorSizeService.createColorSize(productCreateDto.getColorSizes(), product);

    }

    @Override
    public List<ProductDashboardDto> getDashboardProducts() {
        List<Product> products = productRepository.findAll();
        if (!products.isEmpty()) {
            return products.stream().map(product -> modelMapper.map(product, ProductDashboardDto.class)).toList();
        }
        return List.of();
    }

    @Override
    public List<ProductFeaturedDto> getFeaturedProducts() {
        List<Product> products = productRepository.findTop3ByFeaturedTrueOrderByIdDesc();
        if (!products.isEmpty()) {
            return products.stream().map(product -> modelMapper.map(product, ProductFeaturedDto.class)).toList();
        }
        return List.of();
    }

    @Override
    public List<ProductHotTrendDto> getHotTrendProducts() {
        List<Product> products = productRepository.findTop3ByHotTrendingTrueOrderByIdDesc();
        if (!products.isEmpty()) {
            return products.stream().map(product -> modelMapper.map(product, ProductHotTrendDto.class)).toList();
        }
        return List.of();
    }

    @Override
    public List<ProductBestSellerDto> getBestSellerProducts() {
        List<Product> products = productRepository.findAll();
        return List.of();
    }

    @Override
    public Product getProductById(Long productId) {
        return productRepository.findById(productId).orElseThrow();
    }
}
