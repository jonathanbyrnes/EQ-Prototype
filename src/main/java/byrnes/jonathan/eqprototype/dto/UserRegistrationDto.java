package byrnes.jonathan.eqprototype.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@AllArgsConstructor
public class UserRegistrationDto {

    @NotBlank(message =  "You must enter an email.")
    @Email(message = "Please enter a valid email.")
    private String email;

    @NotBlank(message = "You must enter a password.")
    private String password;

}
