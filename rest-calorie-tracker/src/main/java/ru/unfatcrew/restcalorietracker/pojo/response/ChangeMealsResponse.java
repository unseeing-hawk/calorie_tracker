package ru.unfatcrew.restcalorietracker.pojo.response;

import ru.unfatcrew.restcalorietracker.pojo.dto.MealDTO;

import java.util.ArrayList;
import java.util.List;


public class ChangeMealsResponse {
    private List<MealDTO> mealsForChange;
    private List<MealDTO> mealsForDeletion;

    public ChangeMealsResponse() {
        this.mealsForChange = new ArrayList<>();
        this.mealsForDeletion = new ArrayList<>();
    }

    public ChangeMealsResponse(List<MealDTO> mealsForChange, List<MealDTO> mealsForDeletion) {
        this.mealsForChange = mealsForChange;
        this.mealsForDeletion = mealsForDeletion;
    }

    public List<MealDTO> getMealsForChange() {
        return mealsForChange;
    }

    public List<MealDTO> getMealsForDeletion() {
        return mealsForDeletion;
    }

    public void setMealsForChange(List<MealDTO> mealsForChange) {
        this.mealsForChange = mealsForChange;
    }

    public void setMealsForDeletion(List<MealDTO> mealsForDeletion) {
        this.mealsForDeletion = mealsForDeletion;
    }
}
