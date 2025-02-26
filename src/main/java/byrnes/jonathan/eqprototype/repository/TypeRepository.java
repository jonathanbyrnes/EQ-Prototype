package byrnes.jonathan.eqprototype.repository;

import byrnes.jonathan.eqprototype.model.Type;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TypeRepository extends MongoRepository<Type, String> {
}
