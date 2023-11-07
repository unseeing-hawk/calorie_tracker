package ru.unfatcrew.restcalorietracker.pojo.entity;

import jakarta.persistence.*;

@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="user_id")
    private long id;

    @Column(name="user_name")
    private String name;

    @Column(name="user_login")
    private String login;

    @Column(name="user_password")
    private String password;

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
