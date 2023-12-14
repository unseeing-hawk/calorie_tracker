package ru.unfatcrew.restcalorietracker.pojo.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import ru.unfatcrew.restcalorietracker.pojo.dto.MealPutDataDto;

import java.util.ArrayList;
import java.util.List;


public class ChangeMealsRequest {

    @NotNull
    private List<@Valid MealPutDataDto> mealsForChange;

    @NotNull
    private List<@Positive Long> mealIdsForDeletion;

    public ChangeMealsRequest() {

    }

    public ChangeMealsRequest(List<MealPutDataDto> mealsForChange, List<Long> mealIdsForDeletion) {
        this.mealsForChange = mealsForChange;
        this.mealIdsForDeletion = mealIdsForDeletion;
    }

    public List<MealPutDataDto> getMealsForChange() {
        return mealsForChange;
    }

    public void setMealsForChange(List<MealPutDataDto> mealsForChange) {
        this.mealsForChange = mealsForChange;
    }

    public List<Long> getMealIdsForDeletion() {
        return mealIdsForDeletion;
    }

    public void setMealIdsForDeletion(List<Long> mealIdsForDeletion) {
        this.mealIdsForDeletion = mealIdsForDeletion;
    }
}
