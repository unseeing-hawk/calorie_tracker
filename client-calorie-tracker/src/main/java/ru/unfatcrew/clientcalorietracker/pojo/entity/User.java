package ru.unfatcrew.clientcalorietracker.pojo.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

public class User {
    @Positive
    private Long id;
    @NotBlank
    @Size(min = 1, max = 100)
    @Pattern(regexp = "([A-Z][a-zA-Z]*[ ])*([A-Z][a-zA-Z]*)")
    private String name;
    @NotBlank
    @Size(min = 8, max = 30)
    private String login;
    @NotBlank
    @Size(min = 8, max = 50)
    private String password;
    @Pattern(regexp = "[0-9]{1,3}(\\.[0-9]{1,2})?")
    private String weight;

    public User() {

    }

    public User(String name, String login, String password, String weight) {
        this.name = name;
        this.login = login;
        this.password = password;
        this.weight = weight;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}
