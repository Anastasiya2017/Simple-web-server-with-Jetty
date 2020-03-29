package dbService.dataSets;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "personages")
public class Personage implements Serializable { // Serializable Important to Hibernate!
    private static final long serialVersionUID = -8706689714326132798L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "personageId", unique = true)
    private String personageId;

    @Column(name = "status")
    private boolean status;

    @Column(name = "login")
    private String login;

    @Column(name = "name")
    private String name;

    @Column(name = "img")
    private String img;

    @Column(name = "level")
    private String level;

    @Column(name = "x")
    private String x;

    @Column(name = "y")
    private String y;

    //Important to Hibernate!
    @SuppressWarnings("UnusedDeclaration")
    public Personage() {
    }

    public Personage(String name,String login, String img, String personageId, String x, String y) {
        this.setId(-1);
        this.setName(name);
        this.setLogin(login);
        this.setPersonageId(personageId);
        this.setImg(img);
        this.setX(x);
        this.setY(y);
    }

    public Personage(String personageId, String login, boolean status, String img, String level, String x, String y) {
        this.setId(-1);
        this.setPersonageId(personageId);
        this.setLogin(login);
        this.setStatus(status);
        this.setImg(img);
        this.setLevel(level);
        this.setX(x);
        this.setY(y);
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getPersonageId() {
        return personageId;
    }

    public void setPersonageId(String personageId) {
        this.personageId = personageId;
    }

    @SuppressWarnings("UnusedDeclaration")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    //String personageId, boolean status, String img, String level, String x, String y
    @Override
    public String toString() {
        return "Personage{" +
                "id=" + id +
                ", personageId='" + personageId + '\'' +
                ", login='" + login + '\'' +
                ", status='" + status + '\'' +
                ", img='" + img + '\'' +
                ", level='" + level + '\'' +
                ", x='" + x + '\'' +
                ", y='" + y + '\'' +
                '}';
    }
}