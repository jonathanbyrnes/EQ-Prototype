package byrnes.jonathan.eqprototype.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
@Setter
@Document(collection = "responses")
public class Response {

    @Id
    private String id = UUID.randomUUID().toString();

    @NonNull
    private String linkedQuizId;

    @NonNull
    private String questionId;

    @NonNull
    private String responseStr;

    @NonNull
    private boolean isCorrect;

    @NonNull
    private Date answerTime;


}
