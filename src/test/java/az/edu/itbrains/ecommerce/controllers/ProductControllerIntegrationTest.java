package az.edu.itbrains.ecommerce.controllers;

import az.edu.itbrains.ecommerce.dtos.category.CategoryDto;
import az.edu.itbrains.ecommerce.dtos.product.ProductCreateDto;
import az.edu.itbrains.ecommerce.dtos.product.ProductDashboardDto;
import az.edu.itbrains.ecommerce.dtos.product.ProductUpdateDto;
import az.edu.itbrains.ecommerce.exceptions.ResourceNotFoundException;
import az.edu.itbrains.ecommerce.services.CategoryService;
import az.edu.itbrains.ecommerce.services.ColorService;
import az.edu.itbrains.ecommerce.services.ProductService;
import az.edu.itbrains.ecommerce.services.SizeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@SuppressWarnings("null")
class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;
    @MockitoBean
    private CategoryService categoryService;
    @MockitoBean
    private ColorService colorService;
    @MockitoBean
    private SizeService sizeService;

    // ─── GET /dashboard/product ───────────────────────────────────────────────

    @Test
    @DisplayName("GET /dashboard/product: anonim istifadəçi login-ə yönləndirilir")
    void getProductIndex_anonymous_redirectsToLogin() throws Exception {
        mockMvc.perform(get("/dashboard/product"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /dashboard/product: ADMIN rolu ilə 200 qaytarır, products modelə əlavə olunur")
    void getProductIndex_asAdmin_returnsPage() throws Exception {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(1L);
        categoryDto.setName("Shoes");

        ProductDashboardDto dto = new ProductDashboardDto();
        dto.setId(1L);
        dto.setName("Test Product");
        dto.setPrice(99.99);
        dto.setCategory(categoryDto);

        when(productService.getDashboardProducts()).thenReturn(List.of(dto));

        mockMvc.perform(get("/dashboard/product"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/product/index.html"))
                .andExpect(model().attributeExists("products"));

        verify(productService).getDashboardProducts();
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("GET /dashboard/product: USER rolu ilə 403 qaytarır")
    void getProductIndex_asUser_returns403() throws Exception {
        mockMvc.perform(get("/dashboard/product"))
                .andExpect(status().isForbidden());
    }

    // ─── GET /dashboard/product/create ───────────────────────────────────────

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /dashboard/product/create: ADMIN üçün create səhifəsini açır")
    void getProductCreate_asAdmin_returnsCreatePage() throws Exception {
        when(colorService.getAllColors()).thenReturn(List.of());
        when(sizeService.getAllSizes()).thenReturn(List.of());
        when(categoryService.getAllCategories()).thenReturn(List.of());

        mockMvc.perform(get("/dashboard/product/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/product/create.html"))
                .andExpect(model().attributeExists("colors", "sizes", "categories"));
    }

    // ─── POST /dashboard/product/create ──────────────────────────────────────

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /dashboard/product/create: uğurlu yaradılmadan sonra siyahıya yönləndirir")
    void postProductCreate_asAdmin_redirectsToList() throws Exception {
        doNothing().when(productService).createProduct(any(ProductCreateDto.class));

        mockMvc.perform(post("/dashboard/product/create")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "New Shoes")
                        .param("price", "79.99")
                        .param("discount", "0")
                        .param("barcode", "SHOE1234")
                        .param("categoryId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard/product"));

        verify(productService).createProduct(any(ProductCreateDto.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /dashboard/product/create: CSRF token olmadan 403 qaytarır")
    void postProductCreate_withoutCsrf_returns403() throws Exception {
        mockMvc.perform(post("/dashboard/product/create")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "Illegal Product"))
                .andExpect(status().isForbidden());
    }

    // ─── GET /dashboard/product/update/{id} ──────────────────────────────────

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /dashboard/product/update/{id}: mövcud məhsul üçün update səhifəsini qaytarır")
    void getProductUpdate_existingId_returnsUpdatePage() throws Exception {
        az.edu.itbrains.ecommerce.models.Product product = new az.edu.itbrains.ecommerce.models.Product();
        product.setId(1L);
        product.setName("Old Name");

        when(productService.getProductById(1L)).thenReturn(product);
        when(colorService.getAllColors()).thenReturn(List.of());
        when(sizeService.getAllSizes()).thenReturn(List.of());
        when(categoryService.getAllCategories()).thenReturn(List.of());

        mockMvc.perform(get("/dashboard/product/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/product/update.html"))
                .andExpect(model().attributeExists("product"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /dashboard/product/update/{id}: mövcud olmayan id üçün exception propagate olur")
    void getProductUpdate_nonExistingId_throwsException() throws Exception {
        when(productService.getProductById(999L))
                .thenThrow(new ResourceNotFoundException(999L, "Product"));

        // ResourceNotFoundException → GlobalExceptionHandler 404 qaytarır
        mockMvc.perform(get("/dashboard/product/update/999"))
                .andExpect(status().isNotFound());
    }

    // ─── POST /dashboard/product/update/{id} ─────────────────────────────────

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /dashboard/product/update/{id}: yeniləmədən sonra siyahıya yönləndirir")
    void postProductUpdate_validData_redirectsToList() throws Exception {
        doNothing().when(productService).updateProduct(eq(1L), any(ProductUpdateDto.class));

        mockMvc.perform(post("/dashboard/product/update/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "Updated Shoes")
                        .param("price", "150.0")
                        .param("discount", "10")
                        .param("barcode", "UPDT1234")
                        .param("categoryId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard/product"));

        verify(productService).updateProduct(eq(1L), any(ProductUpdateDto.class));
    }

    // ─── POST /dashboard/product/delete/{id} ─────────────────────────────────

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /dashboard/product/delete/{id}: silinmədən sonra siyahıya yönləndirir")
    void postProductDelete_existingId_redirectsToList() throws Exception {
        when(productService.deleteProduct(1L)).thenReturn(true);

        mockMvc.perform(post("/dashboard/product/delete/1")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard/product"));

        verify(productService).deleteProduct(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /dashboard/product/delete/{id}: silmə təsdiq səhifəsini qaytarır")
    void getProductDeleteConfirm_asAdmin_returnsDeletePage() throws Exception {
        az.edu.itbrains.ecommerce.models.Product product = new az.edu.itbrains.ecommerce.models.Product();
        product.setId(1L);
        product.setName("To Delete");

        when(productService.getProductById(1L)).thenReturn(product);

        mockMvc.perform(get("/dashboard/product/delete/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/product/delete.html"))
                .andExpect(model().attribute("productId", 1L))
                .andExpect(model().attribute("productName", "To Delete"));
    }
}
