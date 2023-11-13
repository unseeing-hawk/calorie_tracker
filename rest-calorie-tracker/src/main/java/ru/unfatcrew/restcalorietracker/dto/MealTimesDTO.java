package ru.unfatcrew.restcalorietracker.dto;

public class MealTimesDTO {
    private Long mealTimeId;
    private String[] mealTimeName;

     public Long getMealTimeId() {
        return mealTimeId;
    }

    public String[] getMealTimeName() {
        return mealTimeName;
    }

    public void setMealTimeId(Long mealTimeId) {
        this.mealTimeId = mealTimeId;
    }

    public void setMealTimeName(String[] mealTimeName) {
        this.mealTimeName = mealTimeName;
    }
}