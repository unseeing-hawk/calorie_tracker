package ru.unfatcrew.restcalorietracker.pojo.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import ru.unfatcrew.restcalorietracker.validation.annotation.FiniteFloat;

public class ProductPutDTO {
    @Positive
    private long id;

    @NotBlank
    @Size(min=1, max=100)
    @Column(name="product_name")
    private String name;

    @Min(0)
    private int calories;

    @Min(0)
    @FiniteFloat
    private float proteins;
    
    @Min(0)
    @FiniteFloat
    private float fats;
    
    @Min(0)
    @FiniteFloat
    private float carbohydrates;

    public ProductPutDTO() {
    }

    public ProductPutDTO(@Positive long id, @NotBlank @Size(min = 1, max = 100) String name, @Min(0) int calories,
            @Positive float proteins, @Positive float fats, @Positive float carbohydrates) {
        this.id = id;
        this.name = name;
        this.calories = calories;
        this.proteins = proteins;
        this.fats = fats;
        this.carbohydrates = carbohydrates;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCalories() {
        return calories;
    }

    public float getProteins() {
        return proteins;
    }

    public float getFats() {
        return fats;
    }

    public float getCarbohydrates() {
        return carbohydrates;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public void setProteins(float proteins) {
        this.proteins = proteins;
    }

    public void setFats(float fats) {
        this.fats = fats;
    }

    public void setCarbohydrates(float carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    

}
