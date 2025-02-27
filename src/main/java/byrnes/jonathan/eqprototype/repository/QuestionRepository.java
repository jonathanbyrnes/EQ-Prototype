package byrnes.jonathan.eqprototype.repository;

import byrnes.jonathan.eqprototype.model.Question;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends MongoRepository<Question, String> {
}
