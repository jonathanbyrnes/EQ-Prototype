package byrnes.jonathan.eqprototype.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "categories")
@RequiredArgsConstructor
@Getter
@Setter
public class Category {

    @Id
    private String id = UUID.randomUUID().toString();

    @NonNull
    @NotBlank
    private String name;
}
