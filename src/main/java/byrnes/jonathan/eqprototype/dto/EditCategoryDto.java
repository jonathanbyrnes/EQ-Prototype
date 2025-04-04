package byrnes.jonathan.eqprototype.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class EditCategoryDto {

    @NotBlank(message = "You must enter a name.")
    private String name;

}
