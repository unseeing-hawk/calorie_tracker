package ru.unfatcrew.restcalorietracker.pojo.dto;

import jakarta.validation.constraints.*;
import ru.unfatcrew.restcalorietracker.pojo.entity.Meal;
import ru.unfatcrew.restcalorietracker.validation.annotation.FiniteFloat;
import ru.unfatcrew.restcalorietracker.validation.annotation.LessThan10YearOldDate;

public class MealPostDTO {

    @NotNull
    @Min(0)
    @Max(0)
    private Long id;

    @Size(min=8, max=30)
    private String userLogin;

    @NotNull
    @Positive
    private Long productId;

    @Positive
    @FiniteFloat
    private Float weight;

    @Size(min=10, max=10)
    @LessThan10YearOldDate
    private String date;

    @NotBlank
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

    public MealPostDTO(Meal meal) {
        this.id = meal.getId();
        this.userLogin = meal.getUser().getLogin();
        this.productId = meal.getProduct().getId();
        this.weight = meal.getWeight();
        this.date = meal.getDate().toString();
        this.mealTime = meal.getMealTime().getName();
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
