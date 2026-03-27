package az.edu.itbrains.ecommerce.helpers;

import az.edu.itbrains.ecommerce.models.*;
import az.edu.itbrains.ecommerce.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@SuppressWarnings("null")
public class DataSeeder implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ColorRepository colorRepository;
    private final SizeRepository sizeRepository;
    private final PhotoRepository photoRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        seedRoles();
        seedAdminUser();
        seedCategories();
        seedColors();
        seedSizes();
        seedProducts();
    }

    private void seedRoles() {
        if (roleRepository.count() > 0) {
            return;
        }
        Role adminRole = new Role();
        adminRole.setName("ROLE_ADMIN");
        Role userRole = new Role();
        userRole.setName("ROLE_USER");
        roleRepository.saveAll(Arrays.asList(adminRole, userRole));
    }

    private void seedAdminUser() {
        if (userRepository.findByEmail("admin@admin.com") != null) {
            return;
        }
        Role adminRole = roleRepository.findByName("ROLE_ADMIN");
        User admin = new User();
        admin.setName("Admin");
        admin.setSurname("Admin");
        admin.setEmail("admin@admin.com");
        admin.setPassword(passwordEncoder.encode("Admin@123"));
        List<Role> roles = new ArrayList<>();
        if (adminRole != null) {
            roles.add(adminRole);
        }
        admin.setRoles(roles);
        userRepository.save(admin);
    }

    private void seedCategories() {
        if (categoryRepository.count() > 0) {
            return;
        }
        Category electronics = Category.builder()
                .name("Electronics")
                .seoUrl("electronics")
                .build();
        Category clothing = Category.builder()
                .name("Clothing")
                .seoUrl("clothing")
                .build();
        categoryRepository.saveAll(Arrays.asList(electronics, clothing));
    }

    private void seedColors() {
        if (colorRepository.count() > 0) {
            return;
        }
        Color red = Color.builder().name("Qırmızı").build();
        Color blue = Color.builder().name("Göy").build();
        colorRepository.saveAll(Arrays.asList(red, blue));
    }

    private void seedSizes() {
        if (sizeRepository.count() > 0) {
            return;
        }
        Size small = Size.builder().size("Small").build();
        Size medium = Size.builder().size("Medium").build();
        Size large = Size.builder().size("Large").build();
        sizeRepository.saveAll(Arrays.asList(small, medium, large));
    }

    private void seedProducts() {
        if (productRepository.count() > 0) {
            return;
        }
        Category category = categoryRepository.findAll().stream().findFirst().orElse(null);
        if (category == null) {
            return;
        }
        Product product = Product.builder()
                .name("Demo Product")
                .description("Demo description for product seeding.")
                .shortDescription("Demo short description.")
                .specification("Demo specification.")
                .price(49.99)
                .discount(5)
                .barcode("DEMO-0001")
                .category(category)
                .build();
        productRepository.save(product);

        Photo photo = Photo.builder()
                .url("/front/img/product/product-1.jpg")
                .selected(true)
                .product(product)
                .build();
        photoRepository.save(photo);
    }
}