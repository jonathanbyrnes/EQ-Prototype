package byrnes.jonathan.eqprototype.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.UUID;

@Document(collection = "passwordresettokens")
@Getter
@Setter
@RequiredArgsConstructor
public class PasswordResetToken {

    @Id
    private String id = UUID.randomUUID().toString();

    @NonNull
    private String userId;

    @NonNull
    private String token;

    @NonNull
    private Date expiryDate;
}
