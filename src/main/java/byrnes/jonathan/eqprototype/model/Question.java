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
    @DBRef
    private Quiz quiz;

    @NonNull
    @DBRef
    private Type type;

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

}