package ru.unfatcrew.restcalorietracker.pojo.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name="meals")
public class Meal {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="meal_id")
    private long id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="product_id")
    private Product product;

    @Column(name="meal_productWeight")
    private float weight;

    @Temporal(TemporalType.DATE)
    @Column(name="meal_date")
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name="mealTime_id")
    private MealTime mealTime;

    public Meal() {

    }

    public Meal(User user, Product product, float weight, LocalDate date, MealTime mealTime) {
        this.user = user;
        this.product = product;
        this.weight = weight;
        this.date = date;
        this.mealTime = mealTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public MealTime getMealTime() {
        return mealTime;
    }

    public void setMealTime(MealTime mealTime) {
        this.mealTime = mealTime;
    }
}
