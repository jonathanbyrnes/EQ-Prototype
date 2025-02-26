package byrnes.jonathan.eqprototype.service;

import byrnes.jonathan.eqprototype.dto.CreateQuizDto;
import byrnes.jonathan.eqprototype.model.Category;
import byrnes.jonathan.eqprototype.model.Quiz;
import byrnes.jonathan.eqprototype.model.User;
import byrnes.jonathan.eqprototype.repository.CategoryRepository;
import byrnes.jonathan.eqprototype.repository.QuizRepository;
import byrnes.jonathan.eqprototype.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class QuizService {

    private final QuizRepository quizRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public QuizService(QuizRepository quizRepository, UserRepository userRepository, CategoryRepository categoryRepository) {
        this.quizRepository = quizRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    public Quiz create(String userId, String categoryId, CreateQuizDto createQuizDto) {
        Optional<User> userOptional = this.userRepository.findById(userId);
        if(userOptional.isEmpty()) {
            throw new IllegalArgumentException("User cannot be found.");
        }
        User user = userOptional.get();

        Optional<Category> categoryOptional = this.categoryRepository.findById(categoryId);
        if(categoryOptional.isEmpty()) {
            throw new IllegalArgumentException("Category cannot be found.");
        }
        Category category = categoryOptional.get();

        Quiz quiz = new Quiz(user, category, createQuizDto.getTitle(), createQuizDto.getDescription(),
                createQuizDto.isActive(), new Date());

        return this.quizRepository.save(quiz);
    }
}
