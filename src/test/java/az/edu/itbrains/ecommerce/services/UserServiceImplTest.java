package az.edu.itbrains.ecommerce.services;

import az.edu.itbrains.ecommerce.dtos.auth.RegisterDto;
import az.edu.itbrains.ecommerce.dtos.basket.BasketUserDto;
import az.edu.itbrains.ecommerce.models.Basket;
import az.edu.itbrains.ecommerce.models.Role;
import az.edu.itbrains.ecommerce.models.User;
import az.edu.itbrains.ecommerce.repositories.BasketRepository;
import az.edu.itbrains.ecommerce.repositories.RoleRepository;
import az.edu.itbrains.ecommerce.repositories.UserRepository;
import az.edu.itbrains.ecommerce.services.impls.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("null")
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private BasketRepository basketRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private RegisterDto registerDto;

    @BeforeEach
    void setUp() {
        registerDto = new RegisterDto();
        registerDto.setName("Nurlan");
        registerDto.setSurname("Aliyev");
        registerDto.setEmail("nurlan@test.com");
        registerDto.setPassword("Test@1234");
        registerDto.setConfirmPassword("Test@1234");
    }

    // ─── registerUser ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("registerUser: yeni istifadəçi uğurla qeydiyyatdan keçir, true qaytarır")
    void registerUser_newEmail_savesUserAndReturnsTrue() {
        Role role = new Role(1L, "ROLE_USER");

        when(userRepository.findByEmail("nurlan@test.com")).thenReturn(null);
        when(roleRepository.findByName("ROLE_USER")).thenReturn(role);
        when(passwordEncoder.encode("Test@1234")).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        boolean result = userService.registerUser(registerDto);

        assertThat(result).isTrue();

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User saved = captor.getValue();
        assertThat(saved.getEmail()).isEqualTo("nurlan@test.com");
        assertThat(saved.getName()).isEqualTo("Nurlan");
        assertThat(saved.getPassword()).isEqualTo("hashedPassword");
        assertThat(saved.getRoles()).hasSize(1);
        assertThat(saved.getRoles().get(0).getName()).isEqualTo("ROLE_USER");
    }

    @Test
    @DisplayName("registerUser: dublikat email üçün false qaytarır, save çağrılmır")
    void registerUser_duplicateEmail_returnsFalseWithoutSaving() {
        User existing = new User();
        existing.setEmail("nurlan@test.com");

        when(userRepository.findByEmail("nurlan@test.com")).thenReturn(existing);

        boolean result = userService.registerUser(registerDto);

        assertThat(result).isFalse();
        verify(userRepository, never()).save(any());
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    @DisplayName("registerUser: ROLE_USER tapılmadıqda da istifadəçi yaradılır (boş rol siyahısı)")
    void registerUser_roleNotFound_savesUserWithEmptyRoles() {
        when(userRepository.findByEmail("nurlan@test.com")).thenReturn(null);
        when(roleRepository.findByName("ROLE_USER")).thenReturn(null);
        when(passwordEncoder.encode(anyString())).thenReturn("hashed");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        boolean result = userService.registerUser(registerDto);

        assertThat(result).isTrue();
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        assertThat(captor.getValue().getRoles()).isEmpty();
    }

    // ─── getUserBasket ────────────────────────────────────────────────────────

    @Test
    @DisplayName("getUserBasket: istifadəçi tapıldıqda səbəti qaytarır")
    void getUserBasket_existingUser_returnsBasketDtos() {
        User user = new User();
        user.setId(1L);
        user.setEmail("nurlan@test.com");

        Basket basket = new Basket();
        basket.setId(1L);
        basket.setQuantity(2);

        BasketUserDto basketUserDto = new BasketUserDto();
        basketUserDto.setId(1L);
        basketUserDto.setQuantity(2);

        when(userRepository.findByEmail("nurlan@test.com")).thenReturn(user);
        when(basketRepository.findByUserIdWithProduct(1L)).thenReturn(List.of(basket));
        when(modelMapper.map(basket, BasketUserDto.class)).thenReturn(basketUserDto);

        List<BasketUserDto> result = userService.getUserBasket("nurlan@test.com");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getQuantity()).isEqualTo(2);
    }

    @Test
    @DisplayName("getUserBasket: istifadəçi tapılmadıqda boş list qaytarır (NPE yoxdur)")
    void getUserBasket_unknownUser_returnsEmptyList() {
        when(userRepository.findByEmail("unknown@test.com")).thenReturn(null);

        List<BasketUserDto> result = userService.getUserBasket("unknown@test.com");

        assertThat(result).isEmpty();
        verify(basketRepository, never()).findByUserIdWithProduct(any());
    }

    // ─── getByEmail ───────────────────────────────────────────────────────────

    @Test
    @DisplayName("getByEmail: mövcud email üçün User qaytarır")
    void getByEmail_existingEmail_returnsUser() {
        User user = new User();
        user.setEmail("nurlan@test.com");
        when(userRepository.findByEmail("nurlan@test.com")).thenReturn(user);

        User result = userService.getByEmail("nurlan@test.com");

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("nurlan@test.com");
    }

    @Test
    @DisplayName("getByEmail: mövcud olmayan email üçün null qaytarır")
    void getByEmail_unknownEmail_returnsNull() {
        when(userRepository.findByEmail("ghost@test.com")).thenReturn(null);

        User result = userService.getByEmail("ghost@test.com");

        assertThat(result).isNull();
    }
}
