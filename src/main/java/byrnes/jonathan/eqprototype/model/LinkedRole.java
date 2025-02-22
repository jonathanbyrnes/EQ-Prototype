package byrnes.jonathan.eqprototype.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "linkedroles")
public class LinkedRole {

    @Id
    private String id;

    @DBRef
    private User user;

    @DBRef
    private Role role;

    public LinkedRole(User user, Role role) {
        this.id = UUID.randomUUID().toString();
        this.user = user;
        this.role = role;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Role getRole() {
        return this.role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
