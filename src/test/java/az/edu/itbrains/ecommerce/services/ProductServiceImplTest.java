package az.edu.itbrains.ecommerce.services;

import az.edu.itbrains.ecommerce.dtos.product.ProductCreateDto;
import az.edu.itbrains.ecommerce.dtos.product.ProductDashboardDto;
import az.edu.itbrains.ecommerce.dtos.product.ProductUpdateDto;
import az.edu.itbrains.ecommerce.exceptions.ResourceNotFoundException;
import az.edu.itbrains.ecommerce.models.Category;
import az.edu.itbrains.ecommerce.models.Product;
import az.edu.itbrains.ecommerce.repositories.OrderItemRepository;
import az.edu.itbrains.ecommerce.repositories.ProductRepository;
import az.edu.itbrains.ecommerce.services.impls.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("null")
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private CategoryService categoryService;
    @Mock
    private ColorSizeService colorSizeService;
    @Mock
    private OrderItemRepository orderItemRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;
    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Electronics");

        product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setPrice(99.99);
        product.setDiscount(10.0);
        product.setBarcode("12345678");
        product.setCategory(category);
    }

    // ─── getProductById ───────────────────────────────────────────────────────

    @Test
    @DisplayName("getProductById: mövcud id üçün məhsul qaytarır")
    void getProductById_existingId_returnsProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product result = productService.getProductById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Test Product");
        verify(productRepository).findById(1L);
    }

    @Test
    @DisplayName("getProductById: mövcud olmayan id üçün ResourceNotFoundException atır")
    void getProductById_nonExistingId_throwsResourceNotFoundException() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.getProductById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    @DisplayName("getProductById: null id üçün NullPointerException atır")
    void getProductById_nullId_throwsNullPointerException() {
        assertThatThrownBy(() -> productService.getProductById(null))
                .isInstanceOf(NullPointerException.class);
    }

    // ─── createProduct ────────────────────────────────────────────────────────

    @Test
    @DisplayName("createProduct: düzgün dto ilə məhsul yaradılır")
    void createProduct_validDto_savesProduct() {
        ProductCreateDto dto = new ProductCreateDto();
        dto.setName("New Product");
        dto.setDescription("A description");
        dto.setShortDescription("Short");
        dto.setPrice(50.0);
        dto.setDiscount(5.0);
        dto.setBarcode("ABCD1234");
        dto.setCategoryId(1L);

        when(categoryService.getCategoryById(1L)).thenReturn(category);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        productService.createProduct(dto);

        verify(productRepository).save(any(Product.class));
        verify(categoryService).getCategoryById(1L);
        verify(colorSizeService).createColorSize(any(), any(Product.class));
    }

    // ─── updateProduct ────────────────────────────────────────────────────────

    @Test
    @DisplayName("updateProduct: mövcud məhsul yenilənir")
    void updateProduct_existingProduct_updatesFields() {
        ProductUpdateDto dto = new ProductUpdateDto();
        dto.setName("Updated Name");
        dto.setPrice(120.0);
        dto.setDiscount(15.0);
        dto.setBarcode("UP123456");
        dto.setFeatured(true);
        dto.setHotTrending(false);
        dto.setCategoryId(1L);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(categoryService.getCategoryById(1L)).thenReturn(category);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        productService.updateProduct(1L, dto);

        verify(productRepository).findById(1L);
        verify(productRepository).save(any(Product.class));
        assertThat(product.getName()).isEqualTo("Updated Name");
        assertThat(product.getPrice()).isEqualTo(120.0);
        assertThat(product.isFeatured()).isTrue();
    }

    @Test
    @DisplayName("updateProduct: mövcud olmayan məhsul üçün exception atır")
    void updateProduct_nonExistingProduct_throwsResourceNotFoundException() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.updateProduct(99L, new ProductUpdateDto()))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(productRepository, never()).save(any());
    }

    // ─── deleteProduct ────────────────────────────────────────────────────────

    @Test
    @DisplayName("deleteProduct: mövcud məhsul silinir, true qaytarır")
    void deleteProduct_existingProduct_returnsTrue() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        doNothing().when(productRepository).deleteById(1L);

        boolean result = productService.deleteProduct(1L);

        assertThat(result).isTrue();
        verify(productRepository).deleteById(1L);
    }

    @Test
    @DisplayName("deleteProduct: mövcud olmayan məhsul üçün ResourceNotFoundException atır")
    void deleteProduct_nonExistingProduct_throwsResourceNotFoundException() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.deleteProduct(99L))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(productRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("deleteProduct: null id üçün NullPointerException atır")
    void deleteProduct_nullId_throwsNullPointerException() {
        assertThatThrownBy(() -> productService.deleteProduct(null))
                .isInstanceOf(NullPointerException.class);
    }

    // ─── getDashboardProducts ─────────────────────────────────────────────────

    @Test
    @DisplayName("getDashboardProducts: bütün məhsulları DTO-ya map edir")
    void getDashboardProducts_returnsMappedList() {
        ProductDashboardDto dashboardDto = new ProductDashboardDto();
        dashboardDto.setId(1L);
        dashboardDto.setName("Test Product");

        when(productRepository.findAll()).thenReturn(List.of(product));
        when(modelMapper.map(eq(product), eq(ProductDashboardDto.class))).thenReturn(dashboardDto);

        List<ProductDashboardDto> result = productService.getDashboardProducts();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Test Product");
        verify(productRepository).findAll();
    }
}
