package ru.unfatcrew.restcalorietracker.dto;

import java.util.Date;

public class MealsDTO {
    private Long mealId;
    private Long mealUserId;
    private Long mealProductId;
    private Long mealProductWeight;
    private Date mealDate;
    private Long mealTimeId;

    public Long getMealId() {
        return mealId;
    }

    public Long getMealUserId() {
        return mealUserId;
    }

    public Long getMealProductId() {
        return mealProductId;
    }

    public Long getMealProductWeight() {
        return mealProductWeight;
    }

    public Date getMealDate() {
        return mealDate;
    }

    public Long getMealTimeId() {
        return mealTimeId;
    }

    public void setMealId(Long mealId) {
        this.mealId = mealId;
    }

    public void setMealUserId(Long mealUserId) {
        this.mealUserId = mealUserId;
    }

    public void setMealProductId(Long mealProductId) {
        this.mealProductId = mealProductId;
    }

    public void setMealProductWeight(Long mealProductWeight) {
        this.mealProductWeight = mealProductWeight;
    }

    public void setMealDate(Date mealDate) {
        this.mealDate = mealDate;
    }

    public void setMealTimeId(Long mealTimeId) {
        this.mealTimeId = mealTimeId;
    }
}
