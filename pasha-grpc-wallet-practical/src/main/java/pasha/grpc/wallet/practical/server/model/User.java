package pasha.grpc.wallet.practical.server.model;

import javax.persistence.*;

@Entity(name = "T_USER")
public class User {

    @Id
    private Long id;

    @Column
    private String username ;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
