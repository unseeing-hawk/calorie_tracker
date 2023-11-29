package ru.unfatcrew.restcalorietracker.pojo.entity;

import jakarta.persistence.*;

@Entity
@Table(name="mealtimes")
public class MealTime {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="mealtime_id")
    private long id;

    @Column(name="mealtime_name")
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
