package ru.unfatcrew.clientcalorietracker.pojo.dto.dom;

import ru.unfatcrew.clientcalorietracker.pojo.dto.MealGetDataDTO;

import java.util.ArrayList;
import java.util.List;

public class ChangeMealDTO {
    private List<MealGetDataDTO> mealsToChange;
    private List<Long> idsMealsToDelete;

    public ChangeMealDTO() {
        this.mealsToChange = new ArrayList<>();
        this.idsMealsToDelete = new ArrayList<>();
    }

    public ChangeMealDTO(List<MealGetDataDTO> meals) {
        this.mealsToChange = meals;
        this.idsMealsToDelete = new ArrayList<>();
    }

    public List<MealGetDataDTO> getMealsToChange() {
        return mealsToChange;
    }

    public void setMealsToChange(List<MealGetDataDTO> mealsToChange) {
        this.mealsToChange = mealsToChange;
    }

    public List<Long> getIdsMealsToDelete() {
        return idsMealsToDelete;
    }

    public void setIdsMealsToDelete(List<Long> idsMealsToDelete) {
        this.idsMealsToDelete = idsMealsToDelete;
    }
}
