package ru.unfatcrew.restcalorietracker.pojo.dto;

public class MealDTO {
    private Long id;
    private String userLogin;
    private ProductPostDTO product;
    private Float weight;
    private String date;
    private String mealTime;

    public MealDTO() {
        this.id = 0L;
        this.userLogin = "";
        this.product = new ProductPostDTO();
        this.weight = 0.0f;
        this.date = "";
        this.mealTime = "";
    }

    public MealDTO(Long id, String userLogin, ProductPostDTO product, Float weight, String date, String mealTime) {
        this.id = id;
        this.userLogin = userLogin;
        this.product = product;
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

    public ProductPostDTO getProduct() {
        return product;
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

    public void setProduct(ProductPostDTO product) {
        this.product = product;
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
