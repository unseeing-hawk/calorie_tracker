package ru.unfatcrew.restcalorietracker.pojo.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import ru.unfatcrew.restcalorietracker.pojo.dto.MealPutDataDto;

import java.util.List;


public class ChangeMealsRequest {

    @NotNull
    private List<@Valid MealPutDataDto> mealsForChange;

    @NotNull
    private List<@Positive Long> mealIdsForDeletion;

    @NotBlank
    @Size(min=8, max=30)
    private String userLogin;

    public ChangeMealsRequest() {

    }

    public ChangeMealsRequest(List<MealPutDataDto> mealsForChange, List<Long> mealIdsForDeletion, String userLogin) {
        this.mealsForChange = mealsForChange;
        this.mealIdsForDeletion = mealIdsForDeletion;
        this.userLogin = userLogin;
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

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }
}
