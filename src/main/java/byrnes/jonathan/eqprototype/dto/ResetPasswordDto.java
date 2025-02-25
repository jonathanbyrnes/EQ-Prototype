package byrnes.jonathan.eqprototype.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class ResetPasswordDto {

    @NotBlank(message = "You must enter a password.")
    private String password;
}
