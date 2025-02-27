package byrnes.jonathan.eqprototype.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class ResponseDto {

    @NonNull
    private String response;
}
