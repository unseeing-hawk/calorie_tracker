package ru.unfatcrew.restcalorietracker.pojo.dto;

import jakarta.validation.constraints.Positive;
import ru.unfatcrew.restcalorietracker.validation.annotation.FiniteFloat;

public class MealPostDataDto {

    @Positive
    private long productId;

    @Positive
    @FiniteFloat
    private float weight;

    public MealPostDataDto() {

    }

    public MealPostDataDto(long productId, float weight) {
        this.productId = productId;
        this.weight = weight;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }
}
