package az.edu.itbrains.ecommerce.services.impls;

import az.edu.itbrains.ecommerce.dtos.basket.BasketAddDto;
import az.edu.itbrains.ecommerce.dtos.basket.BasketUserDto;
import az.edu.itbrains.ecommerce.models.Basket;
import az.edu.itbrains.ecommerce.models.Product;
import az.edu.itbrains.ecommerce.models.User;
import az.edu.itbrains.ecommerce.repositories.BasketRepository;
import az.edu.itbrains.ecommerce.services.BasketService;
import az.edu.itbrains.ecommerce.services.ProductService;
import az.edu.itbrains.ecommerce.services.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BasketServiceImpl implements BasketService {

    private final BasketRepository basketRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final ProductService productService;


    @Override
    public void addToCart(String email, BasketAddDto basketAddDto) {
        User user = userService.getByEmail(email);
        Product product = productService.getProductById(basketAddDto.getProductId());
        Basket findBasket = basketRepository.findByUserIdAndProductId(user.getId(), product.getId());
        if(findBasket != null){
          findBasket.setQuantity(findBasket.getQuantity() + 1);
          basketRepository.save(findBasket);
        }else {
            Basket basket = new Basket();
            basket.setUser(user);
            basket.setProduct(product);
            basket.setQuantity(1);
            basket.setQuantity(basket.getQuantity());

            basketRepository.save(basket);
        }


    }

    @Override
    public boolean removeFromBasket(String email, Long productId) {
        User user = userService.getByEmail(email);
        Product product = productService.getProductById(productId);
        Basket findBasket = basketRepository.findByUserIdAndProductId(user.getId(), product.getId());
        if(findBasket != null){
            basketRepository.delete(findBasket);
            return true;
        }
        return false;
    }
}
