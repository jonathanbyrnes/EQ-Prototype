package byrnes.jonathan.eqprototype.service;

import byrnes.jonathan.eqprototype.dto.CreateQuestionDto;
import byrnes.jonathan.eqprototype.dto.EditQuestionDto;
import byrnes.jonathan.eqprototype.model.Question;
import byrnes.jonathan.eqprototype.model.Quiz;
import byrnes.jonathan.eqprototype.repository.QuestionRepository;
import byrnes.jonathan.eqprototype.repository.QuizRepository;
import byrnes.jonathan.eqprototype.repository.TypeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final QuizRepository quizRepository;

    public QuestionService(QuestionRepository questionRepository, QuizRepository quizRepository, TypeRepository typeRepository, QuizRepository quizRepository1) {
        this.questionRepository = questionRepository;
        this.quizRepository = quizRepository1;
    }

    public Question create(String quizId, String typeId,
                           CreateQuestionDto createQuestionDto) {

        Question question = new Question(quizId, typeId,
                createQuestionDto.getQuestionStr(), createQuestionDto.getTimeLimit(),
                createQuestionDto.getWorth(), createQuestionDto.getAnswers(),
                createQuestionDto.getOptions(), createQuestionDto.getQuestionNum(),
                createQuestionDto.getNextQuestionCorrect(), createQuestionDto.getNextQuestionIncorrect(),
                createQuestionDto.getMediaUrl());

        return this.questionRepository.save(question);
    }

    public List<Question> getQuestionsByUserId(String userId) {
        List<Question> bigQuestionList = this.questionRepository.findAll();
        List<Question> questionsByUser = new ArrayList<>();

        for (Question question : bigQuestionList) {
            Optional<Quiz> quizOptional = quizRepository.findById(question.getQuizId());
            if (quizOptional.isPresent()) {
                if (quizOptional.get().getUserId().equals(userId)) {
                    questionsByUser.add(question);
                }
            }
        }

        return questionsByUser;
    }

    public Question edit(String questionId, EditQuestionDto editQuestionDto) {
        Question question = getQuestionById(questionId);

        question.setQuestionStr(editQuestionDto.getQuestionStr());
        question.setTimeLimit(editQuestionDto.getTimeLimit());
        question.setWorth(editQuestionDto.getWorth());
        question.setAnswers(editQuestionDto.getAnswers());
        question.setOptions(editQuestionDto.getOptions());
        question.setTypeId(editQuestionDto.getTypeId());
        question.setNextQuestionIncorrect(editQuestionDto.getNextQuestionIncorrect());
        question.setNextQuestionCorrect(editQuestionDto.getNextQuestionCorrect());
        question.setMediaUrl(editQuestionDto.getMediaUrl());

        return this.questionRepository.save(question);
    }

    public ResponseEntity<Void> delete(String questionId) {
        Question question = getQuestionById(questionId);
        this.questionRepository.delete(question);

        return ResponseEntity.ok().build();
    }

    private Question getQuestionById(String questionId) {
        return questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("Cannot find question."));
    }

}