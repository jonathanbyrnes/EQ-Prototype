package byrnes.jonathan.eqprototype.service;

import byrnes.jonathan.eqprototype.dto.CreateQuestionDto;
import byrnes.jonathan.eqprototype.dto.EditQuestionDto;
import byrnes.jonathan.eqprototype.model.Question;
import byrnes.jonathan.eqprototype.repository.QuestionRepository;
import byrnes.jonathan.eqprototype.repository.QuizRepository;
import byrnes.jonathan.eqprototype.repository.TypeRepository;
import org.springframework.stereotype.Service;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository, QuizRepository quizRepository, TypeRepository typeRepository) {
        this.questionRepository = questionRepository;
    }

    public Question create(String quizId, String typeId,
                           CreateQuestionDto createQuestionDto) {

        Question question = new Question(quizId, typeId,
                createQuestionDto.getQuestionStr(), createQuestionDto.getTimeLimit(),
                createQuestionDto.getWorth(), createQuestionDto.getAnswers(),
                createQuestionDto.getOptions(), createQuestionDto.getQuestionNum());

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

    private Question getQuestionById(String questionId) {
        return questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("Cannot find question."));
    }

}