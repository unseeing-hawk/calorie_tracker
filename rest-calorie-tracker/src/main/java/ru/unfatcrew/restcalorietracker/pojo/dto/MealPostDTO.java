package ru.unfatcrew.restcalorietracker.pojo.dto;

public class MealPostDTO {

    private Long id;
    private String userLogin;
    private Long productId;
    private Float weight;
    private String date;
    private String mealTime;

    public MealPostDTO() {
        this.id = 0L;
        this.userLogin = "";
        this.productId = 0L;
        this.weight = 0.0f;
        this.date = "";
        this.mealTime = "";
    }

    public MealPostDTO(Long id, String userLogin, Long productId, Float weight, String date, String mealTime) {
        this.id = id;
        this.userLogin = userLogin;
        this.productId = productId;
        this.weight = weight;
        this.date = date;
        this.mealTime = mealTime;
    }

    public Long getId() {
        return id;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public Long getProductId() {
        return productId;
    }

    public Float getWeight() {
        return weight;
    }

    public String getDate() {
        return date;
    }

    public String getMealTime() {
        return mealTime;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setMealTime(String mealTime) {
        this.mealTime = mealTime;
    }
}
