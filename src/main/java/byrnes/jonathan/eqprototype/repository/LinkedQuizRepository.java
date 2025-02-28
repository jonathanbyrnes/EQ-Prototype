package byrnes.jonathan.eqprototype.repository;

import byrnes.jonathan.eqprototype.model.LinkedQuiz;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LinkedQuizRepository extends MongoRepository<LinkedQuiz, String> {
    List<LinkedQuiz> findByStatus(String status);
    List<LinkedQuiz> findByUser_Id(String userId);
    List<LinkedQuiz> findByQuiz_Id(String quizId);
    long countByQuiz_Id(String quizId);
}
