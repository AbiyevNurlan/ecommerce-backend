package az.edu.itbrains.ecommerce.services.impls;

import az.edu.itbrains.ecommerce.dtos.auth.RegisterDto;
import az.edu.itbrains.ecommerce.dtos.basket.BasketUserDto;
import az.edu.itbrains.ecommerce.models.Basket;
import az.edu.itbrains.ecommerce.models.User;
import az.edu.itbrains.ecommerce.repositories.UserRepository;
import az.edu.itbrains.ecommerce.services.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;


    @Override
    public boolean registerUser(RegisterDto registerDto) {
        User findUser = userRepository.findByEmail(registerDto.getEmail());
        if (findUser == null) {
            User user = new User();
            user.setEmail(registerDto.getEmail());
            user.setName(registerDto.getName());
            user.setSurname(registerDto.getSurname());
            user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public List<BasketUserDto> getUserBasket(String email) {
        User user = userRepository.findByEmail(email);
        List<Basket> baskets = user.getBaskets();
        List<BasketUserDto> basketUserDtoList = baskets.stream().map(basket -> modelMapper.map(basket, BasketUserDto.class)).toList();
        return basketUserDtoList;
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
