package ru.unfatcrew.restcalorietracker.pojo.dto;

import ru.unfatcrew.restcalorietracker.pojo.entity.Meal;

public class MealGetDataDto {

    private long id;

    private ProductDTO product;

    private float weight;

    private String mealTime;

    public MealGetDataDto() {

    }

    public MealGetDataDto(long id, ProductDTO product, float weight, String mealTime) {
        this.id = id;
        this.product = product;
        this.weight = weight;
        this.mealTime = mealTime;
    }

    public MealGetDataDto(Meal meal) {
        this.id = meal.getId();
        this.product = new ProductDTO(meal.getProduct());
        this.weight = meal.getWeight();
        this.mealTime = meal.getMealTime().getName();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getMealTime() {
        return mealTime;
    }

    public void setMealTime(String mealTime) {
        this.mealTime = mealTime;
    }
}
