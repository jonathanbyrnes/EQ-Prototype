package byrnes.jonathan.eqprototype.service;

import byrnes.jonathan.eqprototype.dto.CreateQuestionDto;
import byrnes.jonathan.eqprototype.dto.EditQuestionDto;
import byrnes.jonathan.eqprototype.model.Question;
import byrnes.jonathan.eqprototype.model.Quiz;
import byrnes.jonathan.eqprototype.model.Type;
import byrnes.jonathan.eqprototype.repository.QuestionRepository;
import byrnes.jonathan.eqprototype.repository.QuizRepository;
import byrnes.jonathan.eqprototype.repository.TypeRepository;
import org.springframework.stereotype.Service;

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

        Quiz quiz = getQuizById(quizId);
        Type type = getTypeById(typeId);

        Question question = new Question(quiz, type,
                createQuestionDto.getQuestionStr(), createQuestionDto.getTimeLimit(),
                createQuestionDto.getWorth(), createQuestionDto.getAnswers(),
                createQuestionDto.getOptions());

        return this.questionRepository.save(question);
    }

    public Question edit(String questionId, EditQuestionDto editQuestionDto) {
        Question question = getQuestionById(questionId);

        question.setQuestionStr(editQuestionDto.getQuestionStr());
        question.setTimeLimit(editQuestionDto.getTimeLimit());
        question.setWorth(editQuestionDto.getWorth());
        question.setAnswers(editQuestionDto.getAnswers());
        question.setOptions(editQuestionDto.getOptions());

        return this.questionRepository.save(question);
    }

    private Quiz getQuizById(String quizId) {
        return quizRepository.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("Quiz cannot be found."));
    }

    private Type getTypeById(String typeId) {
        return typeRepository.findById(typeId)
                .orElseThrow(() -> new IllegalArgumentException("Type cannot be found."));
    }

    private Question getQuestionById(String questionId) {
        return questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("Cannot find question."));
    }

}