package ru.unfatcrew.clientcalorietracker.pojo.dto;

import jakarta.validation.constraints.Positive;

public class MealPostDataDTO {

    @Positive
    private long productId;

    @Positive
    private float weight;

    public MealPostDataDTO() {

    }

    public MealPostDataDTO(long productId, float weight) {
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
