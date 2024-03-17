package ru.unfatcrew.restcalorietracker.pojo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import ru.unfatcrew.restcalorietracker.validation.annotation.FiniteFloat;

@Entity
@Table(name="products")
public class Product {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="product_id")
    private long id;

    @Column(name="product_fatsecret_id")
    private Long fatsecretId;

    @ManyToOne
    @JoinColumn(name="product_user_id")
    private User user;

    @NotBlank
    @Size(min=1, max=100)
    @Column(name="product_name")
    private String name;

    @Min(0)
    @Column(name="product_calories")
    private int calories;

    @Min(0)
    @FiniteFloat
    @Column(name="product_proteins")
    private float proteins;
    
    @Min(0)
    @FiniteFloat
    @Column(name="product_fats")
    private float fats;
    
    @Min(0)
    @FiniteFloat
    @Column(name="product_carbohydrates")
    private float carbohydrates;

    @Column(name="product_is_active")
    private boolean isActive;

    public Product() {}

    public Product(Long fatsecretId,
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
}
