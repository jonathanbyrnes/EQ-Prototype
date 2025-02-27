package byrnes.jonathan.eqprototype.repository;

import byrnes.jonathan.eqprototype.model.Response;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResponseRepository extends MongoRepository<Response, String> {
}
