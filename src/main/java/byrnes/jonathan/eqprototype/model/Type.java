package byrnes.jonathan.eqprototype.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "types")
@Getter
@Setter
@RequiredArgsConstructor
public class Type {

    @Id
    private String id = UUID.randomUUID().toString();

    @NonNull
    @NotBlank
    private String name;
}
