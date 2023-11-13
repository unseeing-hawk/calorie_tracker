package ru.unfatcrew.restcalorietracker.dto;


public class UsersDTO {
    private Long userId;
    private String[] userName;
    private String[] userLogin;
    private String[] userPassword;
    private Double userWeight;

    public Long getUserId() {
        return userId;
    }

    public String[] getUserName() {
        return userName;
    }

    public String[] getUserLogin() {
        return userLogin;
    }

    public String[] getUserPassword() {
        return userPassword;
    }

    public Double getUserWeight() {
        return userWeight;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setUserName(String[] userName) {
        this.userName = userName;
    }

    public void setUserLogin(String[] userLogin) {
        this.userLogin = userLogin;
    }

    public void setUserPassword(String[] userPassword) {
        this.userPassword = userPassword;
    }

    public void setUserWeight(Double userWeight) {
        this.userWeight = userWeight;
    }
}