package byrnes.jonathan.eqprototype.dto;

import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
public class QuizAggregateDto {

    @NonNull
    private String quizId;

    @NonNull
    private int totalStarted;

    @NonNull
    private int totalCompleted;

    @NonNull
    private double averageScore;

    @NonNull
    private int highestScore;

    @NonNull
    private int lowestScore;
}
