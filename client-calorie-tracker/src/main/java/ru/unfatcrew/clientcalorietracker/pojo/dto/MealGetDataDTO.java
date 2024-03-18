package ru.unfatcrew.clientcalorietracker.pojo.dto;

import ru.unfatcrew.clientcalorietracker.pojo.entity.Meal;

import java.util.Objects;

public class MealGetDataDTO {

    private long id;

    private ProductDTO product;

    private float weight;

    private String mealTime;

    public MealGetDataDTO() {

    }

    public MealGetDataDTO(long id, ProductDTO product, float weight, String mealTime) {
        this.id = id;
        this.product = product;
        this.weight = weight;
        this.mealTime = mealTime;
    }

    public MealGetDataDTO(Meal meal) {
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

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        MealGetDataDTO that = (MealGetDataDTO) object;
        return id == that.id &&
                Float.compare(weight, that.weight) == 0 &&
                Objects.equals(product, that.product) &&
                Objects.equals(mealTime, that.mealTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, product, weight, mealTime);
    }
}