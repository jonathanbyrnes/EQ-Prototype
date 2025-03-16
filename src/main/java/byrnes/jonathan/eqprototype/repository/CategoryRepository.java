package byrnes.jonathan.eqprototype.repository;

import byrnes.jonathan.eqprototype.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {
    Optional<Category> findByNameIgnoreCase(String name);
}
