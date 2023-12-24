package ru.unfatcrew.clientcalorietracker.pojo.dto;

import java.time.LocalDate;

public class DaySummaryDTO {
    private LocalDate date;
    private Double weight;
    private Double calories;
    private Double proteins;
    private Double fats;
    private Double carbohydrates;

    public DaySummaryDTO() {
        this.date = LocalDate.now();
        this.weight = 0.0;
        this.calories = 0.0;
        this.proteins = 0.0;
        this.fats = 0.0;
        this.carbohydrates = 0.0;
    }

    public DaySummaryDTO(LocalDate date,
                         Double weight,
                         Double calories,
                         Double proteins,
                         Double fats,
                         Double carbohydrates) {
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

    public Double getWeight() {
        return weight;
    }

    public Double getCalories() {
        return calories;
    }

    public Double getProteins() {
        return proteins;
    }

    public Double getFats() {
        return fats;
    }

    public Double getCarbohydrates() {
        return carbohydrates;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public void setCalories(Double calories) {
        this.calories = calories;
    }

    public void setProteins(Double proteins) {
        this.proteins = proteins;
    }

    public void setFats(Double fats) {
        this.fats = fats;
    }

    public void setCarbohydrates(Double carbohydrates) {
        this.carbohydrates = carbohydrates;
    }
}
