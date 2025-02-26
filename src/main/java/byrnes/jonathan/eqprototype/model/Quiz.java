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

@Document(collection = "quizzes")
@Getter
@Setter
@RequiredArgsConstructor
public class Quiz {

    @Id
    private String id = UUID.randomUUID().toString();

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
    private Date dateOfCreation;


}
