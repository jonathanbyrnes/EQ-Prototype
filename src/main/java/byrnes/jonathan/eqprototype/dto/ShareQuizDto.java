package byrnes.jonathan.eqprototype.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class ShareQuizDto {

    @NotBlank(message = "Link must exist.")
    private String link;

    @NotBlank(message = "QR must exist.")
    private String qrBase64;
}
