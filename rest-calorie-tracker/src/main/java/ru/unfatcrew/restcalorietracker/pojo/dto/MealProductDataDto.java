package ru.unfatcrew.restcalorietracker.pojo.dto;

public class MealProductDataDto {

    private long productId;
    private float weight;

    public MealProductDataDto() {

    }

    public MealProductDataDto(long productId, float weight) {
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
