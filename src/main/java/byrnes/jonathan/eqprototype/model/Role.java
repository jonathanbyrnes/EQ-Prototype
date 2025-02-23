package byrnes.jonathan.eqprototype.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "roles")
@RequiredArgsConstructor
@Getter
@Setter
public class Role {

    @Id
    private String id = UUID.randomUUID().toString();

    @NonNull
    @NotBlank
    private String name;

}
