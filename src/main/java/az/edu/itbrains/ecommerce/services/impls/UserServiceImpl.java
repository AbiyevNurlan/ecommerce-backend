package az.edu.itbrains.ecommerce.services.impls;

import az.edu.itbrains.ecommerce.dtos.auth.RegisterDto;
import az.edu.itbrains.ecommerce.dtos.basket.BasketUserDto;
import az.edu.itbrains.ecommerce.models.Role;
import az.edu.itbrains.ecommerce.models.User;
import az.edu.itbrains.ecommerce.repositories.BasketRepository;
import az.edu.itbrains.ecommerce.repositories.RoleRepository;
import az.edu.itbrains.ecommerce.repositories.UserRepository;
import az.edu.itbrains.ecommerce.services.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BasketRepository basketRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public boolean registerUser(RegisterDto registerDto) {
        User findUser = userRepository.findByEmail(registerDto.getEmail());
        if (findUser == null) {
            Role userRole = roleRepository.findByName("ROLE_USER");
            User user = new User();
            user.setEmail(registerDto.getEmail());
            user.setName(registerDto.getName());
            user.setSurname(registerDto.getSurname());
            user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
            List<Role> roles = new ArrayList<>();
            if (userRole != null) {
                roles.add(userRole);
            }
            user.setRoles(roles);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BasketUserDto> getUserBasket(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return List.of();
        }
        return basketRepository.findByUserIdWithProduct(user.getId()).stream()
                .map(basket -> modelMapper.map(basket, BasketUserDto.class))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public User getByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
