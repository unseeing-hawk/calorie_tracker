package ru.unfatcrew.restcalorietracker.pojo.response;

import ru.unfatcrew.restcalorietracker.pojo.dto.MealGetDto;

import java.util.ArrayList;
import java.util.List;


public class ChangeMealsResponse {
    private MealGetDto mealsForChange;
    private MealGetDto mealsForDeletion;

    public ChangeMealsResponse() {

    }

    public ChangeMealsResponse(MealGetDto mealsForChange, MealGetDto mealsForDeletion) {
        this.mealsForChange = mealsForChange;
        this.mealsForDeletion = mealsForDeletion;
    }

    public MealGetDto getMealsForChange() {
        return mealsForChange;
    }

    public void setMealsForChange(MealGetDto mealsForChange) {
        this.mealsForChange = mealsForChange;
    }

    public MealGetDto getMealsForDeletion() {
        return mealsForDeletion;
    }

    public void setMealsForDeletion(MealGetDto mealsForDeletion) {
        this.mealsForDeletion = mealsForDeletion;
    }
}
