package az.edu.itbrains.ecommerce.repositories;

import az.edu.itbrains.ecommerce.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
