package ru.unfatcrew.restcalorietracker.pojo.response;

import ru.unfatcrew.restcalorietracker.pojo.dto.MealGetDTO;
import ru.unfatcrew.restcalorietracker.pojo.dto.MealPostDTO;

import java.util.ArrayList;
import java.util.List;


public class ChangeMealsResponse {
    private List<MealPostDTO> mealsForChange;
    private List<MealGetDTO> mealsForDeletion;

    public ChangeMealsResponse() {
        this.mealsForChange = new ArrayList<>();
        this.mealsForDeletion = new ArrayList<>();
    }

    public ChangeMealsResponse(List<MealPostDTO> mealsForChange, List<MealGetDTO> mealsForDeletion) {
        this.mealsForChange = mealsForChange;
        this.mealsForDeletion = mealsForDeletion;
    }

    public List<MealPostDTO> getMealsForChange() {
        return mealsForChange;
    }

    public List<MealGetDTO> getMealsForDeletion() {
        return mealsForDeletion;
    }

    public void setMealsForChange(List<MealPostDTO> mealsForChange) {
        this.mealsForChange = mealsForChange;
    }

    public void setMealsForDeletion(List<MealGetDTO> mealsForDeletion) {
        this.mealsForDeletion = mealsForDeletion;
    }
}
