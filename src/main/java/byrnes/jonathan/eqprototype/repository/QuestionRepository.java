package byrnes.jonathan.eqprototype.repository;

import byrnes.jonathan.eqprototype.model.Question;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface QuestionRepository extends MongoRepository<Question, String> {
}
