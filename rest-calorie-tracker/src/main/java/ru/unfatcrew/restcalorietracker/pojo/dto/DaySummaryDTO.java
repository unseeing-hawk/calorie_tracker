package ru.unfatcrew.restcalorietracker.dto;

import java.time.LocalDate;

public class DaySummaryDTO {
    private LocalDate date;
    private Float weight;
    private Integer calories;
    private Float proteins;
    private Float fats;
    private Float carbohydrates;

    public DaySummaryDTO() {
        this.date = LocalDate.now();
        this.weight = 0.0f;
        this.calories = 0;
        this.proteins = 0.0f;
        this.fats = 0.0f;
        this.carbohydrates = 0.0f;
    }
    
    public DaySummaryDTO(LocalDate date, Float weight, Integer calories, Float proteins, Float fats,
            Float carbohydrates) {
        this.date = date;
        this.weight = weight;
        this.calories = calories;
        this.proteins = proteins;
        this.fats = fats;
        this.carbohydrates = carbohydrates;
    }

    public LocalDate getDate() {
        return date;
    }

    public Float getWeight() {
        return weight;
    }

    public Integer getCalories() {
        return calories;
    }

    public Float getProteins() {
        return proteins;
    }

    public Float getFats() {
        return fats;
    }

    public Float getCarbohydrates() {
        return carbohydrates;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public void setProteins(Float proteins) {
        this.proteins = proteins;
    }

    public void setFats(Float fats) {
        this.fats = fats;
    }

    public void setCarbohydrates(Float carbohydrates) {
        this.carbohydrates = carbohydrates;
    }
    

    
}