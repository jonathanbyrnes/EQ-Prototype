package byrnes.jonathan.eqprototype.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

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
}
