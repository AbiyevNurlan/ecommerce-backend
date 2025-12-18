package az.edu.itbrains.ecommerce.repositories;

import az.edu.itbrains.ecommerce.models.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
}
