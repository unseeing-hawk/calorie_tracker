package ru.unfatcrew.clientcalorietracker.pojo.requests;

import ru.unfatcrew.clientcalorietracker.pojo.dto.MealPutDataDTO;

import java.util.List;


public class ChangeMealsRequest {
    private List<MealPutDataDTO> mealsForChange;

    private List<Long> mealIdsForDeletion;

    private String userLogin;

    public ChangeMealsRequest() {

    }

    public ChangeMealsRequest(List<MealPutDataDTO> mealsForChange, List<Long> mealIdsForDeletion, String userLogin) {
        this.mealsForChange = mealsForChange;
        this.mealIdsForDeletion = mealIdsForDeletion;
        this.userLogin = userLogin;
    }

    public ChangeMealsRequest(List<MealPutDataDTO> mealsForChange, List<Long> mealIdsForDeletion) {
        this.mealsForChange = mealsForChange;
        this.mealIdsForDeletion = mealIdsForDeletion;
        this.userLogin = "";
    }

    public List<MealPutDataDTO> getMealsForChange() {
        return mealsForChange;
    }

    public void setMealsForChange(List<MealPutDataDTO> mealsForChange) {
        this.mealsForChange = mealsForChange;
    }

    public List<Long> getMealIdsForDeletion() {
        return mealIdsForDeletion;
    }

    public void setMealIdsForDeletion(List<Long> mealIdsForDeletion) {
        this.mealIdsForDeletion = mealIdsForDeletion;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }
}