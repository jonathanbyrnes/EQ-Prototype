package byrnes.jonathan.eqprototype.model;

import jakarta.validation.constraints.Email;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.UUID;

@Document(collection = "users")
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

        if(isGuest) {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LinkedRole getLinkedRole() {
        return this.linkedRole;
    }

    public void setLinkedRole(LinkedRole linkedRole) {
        this.linkedRole = linkedRole;
    }

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

    public Date getDateOfJoin() {
        return this.dateOfJoin;
    }

    public void setDateOfJoin(Date dateOfJoin) {
        this.dateOfJoin = dateOfJoin;
    }

    public boolean isGuest() {
        return this.isGuest;
    }

    public void setGuest(boolean guest) {
        this.isGuest = guest;
    }

    private void setupGuestId() {
        this.id = "guest_" + UUID.randomUUID();
    }

}
