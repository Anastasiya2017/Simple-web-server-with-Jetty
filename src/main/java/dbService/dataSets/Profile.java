package dbService.dataSets;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "profiles")
public class Profile implements Serializable { // Serializable Important to Hibernate!
    private static final long serialVersionUID = -8706689714326132798L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "login")
    private String login;

    @Column(name = "personageId")
    private String personageId;



    //Important to Hibernate!

    @SuppressWarnings("UnusedDeclaration")
    public Profile() {
    }
    @SuppressWarnings("UnusedDeclaration")
    public Profile(long id, String login) {
        this.setId(id);
        this.setLogin(login);
    }

    public Profile(String login, String personageId) {
        this.setId(-1);
        this.setLogin(login);
        this.setPersonageId(personageId);
    }

    @SuppressWarnings("UnusedDeclaration")
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPersonageId() {
        return personageId;
    }

    public void setPersonageId(String personageId) {
        this.personageId = personageId;
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
                ", personageId='" + personageId + '\'' +
                '}';
    }
}