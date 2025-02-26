package byrnes.jonathan.eqprototype.repository;

import byrnes.jonathan.eqprototype.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CategoryRepository extends MongoRepository<Category, String> {
}
