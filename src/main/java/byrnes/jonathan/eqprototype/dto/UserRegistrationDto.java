package byrnes.jonathan.eqprototype.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserRegistrationDto {

    @NotBlank(message =  "You must enter an email.")
    @Email(message = "Please enter a valid email.")
    private String email;

    @NotBlank(message = "You must enter a password.")
    private String password;

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
