package ru.unfatcrew.clientcalorietracker.pojo.dto;

import ru.unfatcrew.clientcalorietracker.pojo.entity.Product;

import java.util.Objects;

public class ProductDTO {
    private Long id;
    private Long fatsecretId;
    private String userLogin;
    private String name;
    private Integer calories;
    private Float proteins;
    private Float fats;
    private Float carbohydrates;
    private Boolean isActive;

    public ProductDTO() {
        this.id = 0L;
        this.fatsecretId = 0L;
        this.userLogin = "";
        this.name = "";
        this.calories = 0;
        this.proteins = 0.0f;
        this.fats = 0.0f;
        this.carbohydrates = 0.0f;
        this.isActive = false;
    }

    public ProductDTO(Long id, Long fatsecretId, String userLogin, String name, Integer calories, Float proteins,
                      Float fats, Float carbohydrates, Boolean isActive) {
        this.id = id;
        this.fatsecretId = fatsecretId;
        this.userLogin = userLogin;
        this.name = name;
        this.calories = calories;
        this.proteins = proteins;
        this.fats = fats;
        this.carbohydrates = carbohydrates;
        this.isActive = isActive;
    }

    public ProductDTO(Product product) {
        this.id = product.getId();
        this.fatsecretId = product.getFatsecretId();
        this.userLogin = product.getUser() != null
                ? product.getUser().getLogin()
                : null;
        this.name = product.getName();
        this.calories = product.getCalories();
        this.proteins = product.getProteins();
        this.fats = product.getFats();
        this.carbohydrates = product.getCarbohydrates();
        this.isActive = product.isActive();
    }

    public Long getId() {
        return id;
    }

    public Long getFatsecretId() {
        return fatsecretId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public String getName() {
        return name;
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

    public Boolean getIsActive() {
        return isActive;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFatsecretId(Long fatsecretId) {
        this.fatsecretId = fatsecretId;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public void setName(String name) {
        this.name = name;
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

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        ProductDTO that = (ProductDTO) object;
        return Objects.equals(id, that.id) &&
                Objects.equals(fatsecretId, that.fatsecretId) &&
                Objects.equals(userLogin, that.userLogin) &&
                Objects.equals(name, that.name) &&
                Objects.equals(calories, that.calories) &&
                Objects.equals(proteins, that.proteins) &&
                Objects.equals(fats, that.fats) &&
                Objects.equals(carbohydrates, that.carbohydrates) &&
                Objects.equals(isActive, that.isActive);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fatsecretId, userLogin, name, calories, proteins, fats, carbohydrates, isActive);
    }
}
