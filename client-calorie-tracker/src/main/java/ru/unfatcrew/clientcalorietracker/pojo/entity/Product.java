package ru.unfatcrew.clientcalorietracker.pojo.entity;

import jakarta.validation.constraints.*;

import java.util.Objects;

public class Product {
    @Min(0)
    private Long id;

    @Min(0)
    private Long fatsecretId;

    private User user;

    @NotBlank
    @Size(min=1, max=100)
    private String name;

    @Min(0)
    private int calories;

    @DecimalMin(value = "0.00", message = "Proteins must be a positive number with precision up to two decimal places")
    private float proteins;

    @DecimalMin(value = "0.00", message = "Fats must be a positive number with precision up to two decimal places")
    private float fats;

    @DecimalMin(value = "0.00", message = "Carbohydrates must be a positive number with precision up to two decimal places")
    private float carbohydrates;

    private Boolean isActive;

    public Product() {
        this.name = "";
        this.calories = 0;
        this.proteins = 0.0f;
        this.fats = 0.0f;
        this.carbohydrates = 0.0f;
        this.isActive = true;
    }

    public Product(Long fatsecretId, String name, Integer calories, Float proteins,
                   Float fats, Float carbohydrates) {
        this.fatsecretId = fatsecretId;
        this.name = name;
        this.calories = calories;
        this.proteins = proteins;
        this.fats = fats;
        this.carbohydrates = carbohydrates;
        this.isActive = true;
    }

    public Product(User user, String name, Integer calories, Float proteins,
                   Float fats, Float carbohydrates) {
        this.user = user;
        this.name = name;
        this.calories = calories;
        this.proteins = proteins;
        this.fats = fats;
        this.carbohydrates = carbohydrates;
        this.isActive = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFatsecretId() {
        return fatsecretId;
    }

    public void setFatsecretId(Long fatsecretId) {
        this.fatsecretId = fatsecretId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public float getProteins() {
        return proteins;
    }

    public void setProteins(float proteins) {
        this.proteins = proteins;
    }

    public float getFats() {
        return fats;
    }

    public void setFats(float fats) {
        this.fats = fats;
    }

    public float getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(float carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Product product = (Product) object;
        return calories == product.calories &&
                Float.compare(proteins, product.proteins) == 0 &&
                Float.compare(fats, product.fats) == 0 &&
                Float.compare(carbohydrates, product.carbohydrates) == 0 &&
                Objects.equals(id, product.id) &&
                Objects.equals(fatsecretId, product.fatsecretId) &&
                Objects.equals(user, product.user) &&
                Objects.equals(name, product.name) &&
                Objects.equals(isActive, product.isActive);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fatsecretId, user, name, calories, proteins, fats, carbohydrates, isActive);
    }
}
