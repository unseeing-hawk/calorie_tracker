package ru.unfatcrew.clientcalorietracker.pojo.dto;

public class MealPutDataDTO {
    private long id;

    private float weight;

    private String mealTime;

    public MealPutDataDTO() {

    }

    public MealPutDataDTO(long id, float weight, String mealTime) {
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