package ru.unfatcrew.restcalorietracker.pojo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name="products")
public class Product {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="product_id")
    private long id;

    @Min(0)
    @Column(name="product_fatsecretId")
    private long fatsecretId;

    @ManyToOne
    @JoinColumn(name="product_userId")
    private User user;

    @NotBlank
    @Size(min=1, max=100)
    @Column(name="product_name")
    private String name;

    @Min(0)
    @Column(name="product_calories")
    private int calories;

    @DecimalMin(value = "0.00", inclusive = false, message = "Proteins must be a positive number with precision up to two decimal places")
    @Column(name="product_proteins")
    private float proteins;
    
    @DecimalMin(value = "0.00", inclusive = false, message = "Fats must be a positive number with precision up to two decimal places")
    @Column(name="product_fats")
    private float fats;
    
    @DecimalMin(value = "0.00", inclusive = false, message = "Carbohydrates must be a positive number with precision up to two decimal places")
    @Column(name="product_carbohydrates")
    private float carbohydrates;

    @Column(name="product_isActive")
    private boolean isActive;

    public Product() {}

    public Product(long fatsecretId,
                   User user, String name,
                   int calories,
                   float proteins,
                   float fats,
                   float carbohydrates,
                   boolean isActive) {
        this.fatsecretId = fatsecretId;
        this.user = user;
        this.name = name;
        this.calories = calories;
        this.proteins = proteins;
        this.fats = fats;
        this.carbohydrates = carbohydrates;
        this.isActive = isActive;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getFatsecretId() {
        return fatsecretId;
    }

    public void setFatsecretId(long fatsecretId) {
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
}
