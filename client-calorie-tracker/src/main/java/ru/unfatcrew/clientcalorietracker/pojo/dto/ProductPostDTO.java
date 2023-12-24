package ru.unfatcrew.clientcalorietracker.pojo.dto;

import jakarta.validation.constraints.*;

public class ProductPostDTO {
    private String userLogin;

    @NotBlank
    @Size(min=1, max=100)
    private String name;

    @Min(0)
    private int calories;

    @DecimalMin(value = "0.00", inclusive = false, message = "Proteins must be a positive number with precision up to two decimal places")
    private float proteins;

    @DecimalMin(value = "0.00", inclusive = false, message = "Fats must be a positive number with precision up to two decimal places")
    private float fats;

    @DecimalMin(value = "0.00", inclusive = false, message = "Carbohydrates must be a positive number with precision up to two decimal places")
    private float carbohydrates;

    public ProductPostDTO() {
        this.userLogin = "";
        this.name = "";
        this.calories = 0;
        this.proteins = 0.0f;
        this.fats = 0.0f;
        this.carbohydrates = 0.0f;
    }

    public ProductPostDTO(String name, Integer calories, Float proteins,
                          Float fats, Float carbohydrates) {
        this.name = name;
        this.calories = calories;
        this.proteins = proteins;
        this.fats = fats;
        this.carbohydrates = carbohydrates;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public float getProteins() {
        return proteins;
    }

    public void setProteins(float proteins) {
        this.proteins = proteins;
    }

    public float getFats() {
        return fats;
    }

    public void setFats(float fats) {
        this.fats = fats;
    }

    public float getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(float carbohydrates) {
        this.carbohydrates = carbohydrates;
    }
}
