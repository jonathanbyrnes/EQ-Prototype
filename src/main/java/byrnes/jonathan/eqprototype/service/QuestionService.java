package byrnes.jonathan.eqprototype.service;

import byrnes.jonathan.eqprototype.dto.CreateQuestionDto;
import byrnes.jonathan.eqprototype.dto.EditQuestionDto;
import byrnes.jonathan.eqprototype.dto.EditQuizDto;
import byrnes.jonathan.eqprototype.model.Question;
import byrnes.jonathan.eqprototype.model.Quiz;
import byrnes.jonathan.eqprototype.model.Type;
import byrnes.jonathan.eqprototype.repository.QuestionRepository;
import byrnes.jonathan.eqprototype.repository.QuizRepository;
import byrnes.jonathan.eqprototype.repository.TypeRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final QuizRepository quizRepository;
    private final TypeRepository typeRepository;

    public QuestionService(QuestionRepository questionRepository, QuizRepository quizRepository, TypeRepository typeRepository) {
        this.questionRepository = questionRepository;
        this.quizRepository = quizRepository;
        this.typeRepository = typeRepository;
    }

    public Question create(String quizId, String typeId,
                           CreateQuestionDto createQuestionDto) {

        Optional<Quiz> quizOptional = this.quizRepository.findById(quizId);
        if(quizOptional.isEmpty()) {
            throw new IllegalArgumentException("Quiz cannot be found.");
        }
        Quiz quiz = quizOptional.get();

        Optional<Type> typeOptional = this.typeRepository.findById(typeId);
        if(typeOptional.isEmpty()) {
            throw new IllegalArgumentException("Type cannot be found.");
        }
        Type type = typeOptional.get();

        Question question = new Question(quiz, type,
                createQuestionDto.getQuestionStr(), createQuestionDto.getTimeLimit(),
                createQuestionDto.getWorth(), createQuestionDto.getAnswers(),
                createQuestionDto.getOptions());

        return this.questionRepository.save(question);
    }

    public Question edit(String questionId, EditQuestionDto editQuestionDto) {

        return null;
    }

}