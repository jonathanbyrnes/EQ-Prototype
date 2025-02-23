package byrnes.jonathan.eqprototype.model;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.UUID;

@Document(collection = "users")
@Getter
@Setter
public class User {

    @Id
    private String id;

    @DBRef
    private LinkedRole linkedRole;

    @Email
    private String email;

    private String password;

    private Date dateOfJoin;

    private boolean isGuest;

    public User(LinkedRole linkedRole, String email, String password, Date dateOfJoin, boolean isGuest) {

        if (isGuest) {
            this.setupGuestId();
        } else {
            this.id = UUID.randomUUID().toString();
        }

        this.linkedRole = linkedRole;
        this.email = email;
        this.password = password;
        this.dateOfJoin = dateOfJoin;
        this.isGuest = isGuest;
    }

    private void setupGuestId() {
        this.id = "guest_" + UUID.randomUUID();
    }

}
