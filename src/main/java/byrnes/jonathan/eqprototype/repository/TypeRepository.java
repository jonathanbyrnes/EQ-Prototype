package byrnes.jonathan.eqprototype.repository;

import byrnes.jonathan.eqprototype.model.Type;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeRepository extends MongoRepository<Type, String> {
}
