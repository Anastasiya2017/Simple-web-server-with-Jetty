package dbService.dataSets;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "profils")
public class UsersProfiles implements Serializable { // Serializable Important to Hibernate!
    private static final long serialVersionUID = -8706689714326132798L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "login", unique = true, updatable = false)
    private String login;

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    @Column(name = "characterName")
    private String characterName;

    //Important to Hibernate!
    @SuppressWarnings("UnusedDeclaration")
    public UsersProfiles() {
    }

    @SuppressWarnings("UnusedDeclaration")
    public UsersProfiles(long id, String login) {
        this.setId(id);
        this.setLogin(login);
    }

    public UsersProfiles(String login) {
        this.setId(-1);
        this.setLogin(login);
        this.setCharacterName(characterName);
    }

    @SuppressWarnings("UnusedDeclaration")
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return "UserDataSet{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", characterName='" + characterName + '\'' +
                '}';
    }
}