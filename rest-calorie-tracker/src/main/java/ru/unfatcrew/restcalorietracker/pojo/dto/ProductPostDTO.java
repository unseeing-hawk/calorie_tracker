package ru.unfatcrew.restcalorietracker.pojo.dto;

public class ProductPostDTO {
    private Long fatsecretId;
    private String userLogin;
    private String name;
    private Integer calories;
    private Float proteins;
    private Float fats;
    private Float carbohydrates;

    public ProductPostDTO() {
        this.fatsecretId = 0L;
        this.userLogin = "";
        this.name = "";
        this.calories = 0;
        this.proteins = 0.0f;
        this.fats = 0.0f;
        this.carbohydrates = 0.0f;
    }

    public ProductPostDTO(Long fatsecretId, String name, Integer calories, Float proteins,
                      Float fats, Float carbohydrates) {
        this.fatsecretId = fatsecretId;
        this.userLogin = "";
        this.name = name;
        this.calories = calories;
        this.proteins = proteins;
        this.fats = fats;
        this.carbohydrates = carbohydrates;
    }
    
    public ProductPostDTO(String userLogin, String name, Integer calories, Float proteins,
                      Float fats, Float carbohydrates) {
        this.fatsecretId = 0L;
        this.userLogin = userLogin;
        this.name = name;
        this.calories = calories;
        this.proteins = proteins;
        this.fats = fats;
        this.carbohydrates = carbohydrates;
    }

    public Long getFatsecretId() {
        return fatsecretId;
    }


    public String getUserLogin() {
        return userLogin;
    }
    
    public String getName() {
        return name;
    }
    
    public Integer getCalories() {
        return calories;
    }
    
    public Float getProteins() {
        return proteins;
    }
    
    public Float getFats() {
        return fats;
    }
    
    public Float getCarbohydrates() {
        return carbohydrates;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public void setProteins(Float proteins) {
        this.proteins = proteins;
    }

    public void setFats(Float fats) {
        this.fats = fats;
    }

    public void setCarbohydrates(Float carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public void setFatsecretId(Long fatsecretId) {
        this.fatsecretId = fatsecretId;
    }
}
