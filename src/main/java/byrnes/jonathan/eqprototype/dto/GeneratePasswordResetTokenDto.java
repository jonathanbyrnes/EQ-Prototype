package byrnes.jonathan.eqprototype.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class GeneratePasswordResetTokenDto {

    @NotBlank(message = "You must enter an email.")
    @Email(message = "Please enter a valid email.")
    private String email;

}
