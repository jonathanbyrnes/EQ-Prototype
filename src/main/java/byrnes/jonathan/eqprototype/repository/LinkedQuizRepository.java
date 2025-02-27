package byrnes.jonathan.eqprototype.repository;

import byrnes.jonathan.eqprototype.model.LinkedQuiz;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkedQuizRepository extends MongoRepository<LinkedQuiz, String> {
}
