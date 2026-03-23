package az.edu.itbrains.ecommerce.helpers;

import az.edu.itbrains.ecommerce.models.Category;
import az.edu.itbrains.ecommerce.models.Color;
import az.edu.itbrains.ecommerce.models.Photo;
import az.edu.itbrains.ecommerce.models.Product;
import az.edu.itbrains.ecommerce.models.Size;
import az.edu.itbrains.ecommerce.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ColorRepository colorRepository;
    private final SizeRepository sizeRepository;
    private final PhotoRepository photoRepository;

    @Override
    public void run(String... args) {
        seedCategories();
        seedColors();
        seedSizes();
        seedProducts();
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
                .url("https://via.placeholder.com/600")
                .selected(true)
                .product(product)
                .build();
        photoRepository.save(photo);
    }
}