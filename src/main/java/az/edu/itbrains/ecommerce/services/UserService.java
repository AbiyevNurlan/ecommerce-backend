package az.edu.itbrains.ecommerce.services;

import az.edu.itbrains.ecommerce.dtos.auth.RegisterDto;
import az.edu.itbrains.ecommerce.dtos.basket.BasketUserDto;
import az.edu.itbrains.ecommerce.models.User;
import jakarta.validation.Valid;

import java.util.List;

public interface UserService {
    boolean registerUser(RegisterDto registerDto);

    List<BasketUserDto> getUserBasket(String email);

    User getByEmail(String email);
}
