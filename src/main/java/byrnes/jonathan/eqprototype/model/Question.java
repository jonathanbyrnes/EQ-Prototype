package byrnes.jonathan.eqprototype.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

@Document(collection = "questions")
@Getter
@Setter
@RequiredArgsConstructor
public class Question {

    @Id
    private String id = UUID.randomUUID().toString();

    @NonNull
    private String quizId;

    @NonNull
    private String typeId;

    @NonNull
    private String questionStr;

    @NonNull
    private int timeLimit;

    @NonNull
    private int worth;

    @NonNull
    private List<String> answers;

    @NonNull
    private List<String> options;

    @NonNull
    private int questionNum;

    @NonNull
    private String nextQuestionCorrect;

    @NonNull
    private String nextQuestionIncorrect;

    @NonNull
    private String mediaUrl;

}