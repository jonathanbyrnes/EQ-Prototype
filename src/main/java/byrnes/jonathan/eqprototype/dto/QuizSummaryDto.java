package byrnes.jonathan.eqprototype.dto;

import byrnes.jonathan.eqprototype.model.Question;
import byrnes.jonathan.eqprototype.model.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class QuizSummaryDto {

    @NonNull
    private String linkedQuizId;

    @NonNull
    private int totalQuestions;

    @NonNull
    private int correctAnswers;

    @NonNull
    private int score;

    @NonNull
    private List<Question> questionList;

    @NonNull
    private List<Response> responseList;

}
