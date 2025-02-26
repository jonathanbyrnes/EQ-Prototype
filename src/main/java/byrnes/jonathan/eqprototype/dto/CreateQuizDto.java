package byrnes.jonathan.eqprototype.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class CreateQuizDto {

    @NotBlank(message = "You must enter a title.")
    private String title;

    @NotBlank(message = "You must enter a description.")
    private String description;

    @NotNull(message = "You must select an activity status.")
    private boolean isActive;

    @NotNull(message = "You must select a randomised status.")
    private boolean isQuestionsRandomised;

}
