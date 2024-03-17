package ru.unfatcrew.restcalorietracker.pojo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import ru.unfatcrew.restcalorietracker.validation.annotation.FiniteFloat;

@Entity
@Table(name="users", schema="public")
public class User {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="user_id")
    private long id;

    @NotBlank
    @Size(min=1, max=100)
    @Pattern(regexp="([A-Z][a-zA-Z]*[ ])*([A-Z][a-zA-Z]*)")
    @Column(name="user_name")
    private String name;

    @NotBlank
    @Size(min=8, max=30)
    @Column(name="user_login")
    private String login;

    @NotBlank
    @Size(min=68,max=68)
    @Column(name="user_password")
    private String password;

    @Positive
    @FiniteFloat
    @Column(name="user_weight")
    private float weight;

    public User() {

    }

    public User(String name, String login, String password, float weight) {
        this.name = name;
        this.login = login;
        this.password = password;
        this.weight = weight;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }
}
