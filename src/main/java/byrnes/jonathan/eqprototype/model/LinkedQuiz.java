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

@Getter
@Setter
@RequiredArgsConstructor
@Document(collection = "linkedquizzes")
public class LinkedQuiz {

    @Id
    private String id = UUID.randomUUID().toString();

    @NonNull
    @DBRef
    private User user;

    @NonNull
    @DBRef
    private Quiz quiz;

    @NonNull
    private Date dateStarted;

    @NonNull
    private String status;

    @NonNull
    private int score;

    @NonNull
    private Date currentQStartTime;

    @NonNull
    private Date lastActivityTime;
}
