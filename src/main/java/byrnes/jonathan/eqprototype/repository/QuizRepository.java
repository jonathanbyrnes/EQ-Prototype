package byrnes.jonathan.eqprototype.repository;

import byrnes.jonathan.eqprototype.model.Quiz;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizRepository extends MongoRepository<Quiz, String> {
}
