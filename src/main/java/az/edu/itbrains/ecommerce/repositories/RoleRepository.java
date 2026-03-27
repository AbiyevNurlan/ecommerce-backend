package az.edu.itbrains.ecommerce.repositories;

import az.edu.itbrains.ecommerce.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
