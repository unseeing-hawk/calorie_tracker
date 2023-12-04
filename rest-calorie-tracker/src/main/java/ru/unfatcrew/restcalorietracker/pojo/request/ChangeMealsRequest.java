package ru.unfatcrew.restcalorietracker.pojo.request;

import ru.unfatcrew.restcalorietracker.pojo.dto.MealDTO;

import java.util.ArrayList;
import java.util.List;


public class ChangeMealsRequest {
    private List<MealDTO> mealsForChange;
    private List<Long> mealIdsForDeletion;

    public ChangeMealsRequest() {
        this.mealsForChange = new ArrayList<>();
        this.mealIdsForDeletion = new ArrayList<>();
    }

    public ChangeMealsRequest(List<MealDTO> mealsForChange, List<Long> mealIdsForDeletion) {
        this.mealsForChange = mealsForChange;
        this.mealIdsForDeletion = mealIdsForDeletion;
    }

    public List<MealDTO> getMealsForChange() {
        return mealsForChange;
    }

    public List<Long> getMealIdsForDeletion() {
        return mealIdsForDeletion;
    }
    
    public void setMealsForChange(List<MealDTO> mealsForChange) {
        this.mealsForChange = mealsForChange;
    }

    public void setMealIdsForDeletion(List<Long> mealIdsForDeletion) {
        this.mealIdsForDeletion = mealIdsForDeletion;
    }
}
