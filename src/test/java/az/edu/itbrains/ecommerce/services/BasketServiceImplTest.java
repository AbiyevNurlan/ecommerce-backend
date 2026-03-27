package az.edu.itbrains.ecommerce.services;

import az.edu.itbrains.ecommerce.dtos.basket.BasketAddDto;
import az.edu.itbrains.ecommerce.models.Basket;
import az.edu.itbrains.ecommerce.models.Product;
import az.edu.itbrains.ecommerce.models.User;
import az.edu.itbrains.ecommerce.repositories.BasketRepository;
import az.edu.itbrains.ecommerce.services.impls.BasketServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("null")
class BasketServiceImplTest {

    @Mock
    private BasketRepository basketRepository;
    @Mock
    private UserService userService;
    @Mock
    private ProductService productService;

    @InjectMocks
    private BasketServiceImpl basketService;

    private User user;
    private Product product;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@test.com");

        product = new Product();
        product.setId(10L);
        product.setName("Shoes");
        product.setPrice(50.0);
    }

    // ─── addToCart ────────────────────────────────────────────────────────────

    @Test
    @DisplayName("addToCart: səbətdə məhsul yoxdursa yeni basket yaradır")
    void addToCart_itemNotInBasket_createsNewBasket() {
        BasketAddDto dto = new BasketAddDto(10L, 2);

        when(userService.getByEmail("test@test.com")).thenReturn(user);
        when(productService.getProductById(10L)).thenReturn(product);
        when(basketRepository.findByUserIdAndProductId(1L, 10L)).thenReturn(null);

        basketService.addToCart("test@test.com", dto);

        ArgumentCaptor<Basket> captor = ArgumentCaptor.forClass(Basket.class);
        verify(basketRepository).save(captor.capture());
        Basket saved = captor.getValue();
        assertThat(saved.getUser()).isEqualTo(user);
        assertThat(saved.getProduct()).isEqualTo(product);
        assertThat(saved.getQuantity()).isEqualTo(2);
    }

    @Test
    @DisplayName("addToCart: məhsul artıq səbətdədirsə miqdarı artırır")
    void addToCart_itemAlreadyInBasket_incrementsQuantity() {
        BasketAddDto dto = new BasketAddDto(10L, 3);

        Basket existing = new Basket();
        existing.setId(5L);
        existing.setUser(user);
        existing.setProduct(product);
        existing.setQuantity(2);

        when(userService.getByEmail("test@test.com")).thenReturn(user);
        when(productService.getProductById(10L)).thenReturn(product);
        when(basketRepository.findByUserIdAndProductId(1L, 10L)).thenReturn(existing);

        basketService.addToCart("test@test.com", dto);

        verify(basketRepository).save(existing);
        assertThat(existing.getQuantity()).isEqualTo(5); // 2 + 3
    }

    @Test
    @DisplayName("addToCart: miqdar 0 və ya mənfi verilsə minumum 1 ilə yadda saxlanır")
    void addToCart_zeroQuantity_usesMinimumOfOne() {
        BasketAddDto dto = new BasketAddDto(10L, 0);

        when(userService.getByEmail("test@test.com")).thenReturn(user);
        when(productService.getProductById(10L)).thenReturn(product);
        when(basketRepository.findByUserIdAndProductId(1L, 10L)).thenReturn(null);

        basketService.addToCart("test@test.com", dto);

        ArgumentCaptor<Basket> captor = ArgumentCaptor.forClass(Basket.class);
        verify(basketRepository).save(captor.capture());
        assertThat(captor.getValue().getQuantity()).isEqualTo(1);
    }

    // ─── removeFromBasket ─────────────────────────────────────────────────────

    @Test
    @DisplayName("removeFromBasket: məhsul tapıldıqda silinir və true qaytarır")
    void removeFromBasket_existingItem_deletesAndReturnsTrue() {
        Basket basket = new Basket();
        basket.setId(5L);
        basket.setUser(user);
        basket.setProduct(product);

        when(userService.getByEmail("test@test.com")).thenReturn(user);
        when(productService.getProductById(10L)).thenReturn(product);
        when(basketRepository.findByUserIdAndProductId(1L, 10L)).thenReturn(basket);

        boolean result = basketService.removeFromBasket("test@test.com", 10L);

        assertThat(result).isTrue();
        verify(basketRepository).delete(basket);
    }

    @Test
    @DisplayName("removeFromBasket: məhsul tapılmadıqda false qaytarır")
    void removeFromBasket_itemNotFound_returnsFalse() {
        when(userService.getByEmail("test@test.com")).thenReturn(user);
        when(productService.getProductById(10L)).thenReturn(product);
        when(basketRepository.findByUserIdAndProductId(1L, 10L)).thenReturn(null);

        boolean result = basketService.removeFromBasket("test@test.com", 10L);

        assertThat(result).isFalse();
        verify(basketRepository, never()).delete(any());
    }
}
