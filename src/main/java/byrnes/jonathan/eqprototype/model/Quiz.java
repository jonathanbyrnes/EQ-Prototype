package byrnes.jonathan.eqprototype.model;


import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Random;

@Document(collection = "quizzes")
@Getter
@Setter
@RequiredArgsConstructor
public class Quiz {

    @Id
    private String id = generateQuizId();

    @NonNull
    @DBRef
    private User user;

    @NonNull
    @DBRef
    private Category category;

    @NonNull
    private String title;

    @NonNull
    private String description;

    @NonNull
    private boolean isActive;

    @NonNull
    private boolean isQuestionsRandomised;

    @NonNull
    private Date dateOfCreation;

    @NonNull
    private boolean isInstantFeedback;

    private String generateQuizId() {
        int code = 10000 + new Random().nextInt(90000); //5 digit quiz id
        return String.valueOf(code);
    }
}
