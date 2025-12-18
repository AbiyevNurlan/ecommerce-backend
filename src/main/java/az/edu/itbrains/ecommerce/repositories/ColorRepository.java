package az.edu.itbrains.ecommerce.repositories;

import az.edu.itbrains.ecommerce.models.Color;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColorRepository extends JpaRepository<Color, Long> {
}
