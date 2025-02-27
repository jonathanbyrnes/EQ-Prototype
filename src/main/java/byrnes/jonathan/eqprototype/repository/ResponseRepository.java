package byrnes.jonathan.eqprototype.repository;

import byrnes.jonathan.eqprototype.model.Response;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResponseRepository extends MongoRepository<Response, String> {
    List<Response> findByLinkedQuizId(String linkedQuizId);
}
