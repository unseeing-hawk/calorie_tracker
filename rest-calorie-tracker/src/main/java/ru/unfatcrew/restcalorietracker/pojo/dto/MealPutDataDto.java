package ru.unfatcrew.restcalorietracker.pojo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import ru.unfatcrew.restcalorietracker.validation.annotation.FiniteFloat;

public class MealPutDataDto {

    @Positive
    private long id;

    @Positive
    @FiniteFloat
    private float weight;

    @NotBlank
    private String mealTime;

    public MealPutDataDto() {

    }

    public MealPutDataDto(long id, float weight, String mealTime) {
        this.id = id;
        this.weight = weight;
        this.mealTime = mealTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getMealTime() {
        return mealTime;
    }

    public void setMealTime(String mealTime) {
        this.mealTime = mealTime;
    }
}
