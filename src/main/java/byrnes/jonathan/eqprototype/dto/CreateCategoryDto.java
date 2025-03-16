package byrnes.jonathan.eqprototype.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CreateCategoryDto {

    @NotBlank(message = "You must enter a category name.")
    private String name;

}
