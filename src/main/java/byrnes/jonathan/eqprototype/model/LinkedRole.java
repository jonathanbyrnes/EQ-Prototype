package byrnes.jonathan.eqprototype.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "linkedroles")
@Getter
@Setter
@RequiredArgsConstructor
public class LinkedRole {

    @Id
    private String id = UUID.randomUUID().toString();

    @DBRef
    private User user;

    @NonNull
    @DBRef
    private Role role;

}
