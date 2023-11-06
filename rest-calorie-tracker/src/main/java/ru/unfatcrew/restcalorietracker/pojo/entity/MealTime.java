package ru.unfatcrew.restcalorietracker.pojo.entity;

import jakarta.persistence.*;

@Entity
@Table(name="mealTime")
public class MealTime {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="mealTime_id")
    private long id;

    @Column(name="mealTime_name")
    private String name;

    public MealTime() {

    }

    public MealTime(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
