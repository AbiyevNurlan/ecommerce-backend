package az.edu.itbrains.ecommerce.services;

import az.edu.itbrains.ecommerce.dtos.basket.BasketAddDto;

public interface BasketService {
    void addToCart(String email, BasketAddDto basketAddDto);

    boolean removeFromBasket(String email, Long productId);
}
