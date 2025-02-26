package byrnes.jonathan.eqprototype.repository;

import byrnes.jonathan.eqprototype.model.Quiz;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface QuizRepository extends MongoRepository<Quiz, String> {
}
