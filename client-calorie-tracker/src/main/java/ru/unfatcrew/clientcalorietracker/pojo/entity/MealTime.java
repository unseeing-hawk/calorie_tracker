package ru.unfatcrew.clientcalorietracker.pojo.entity;

public class MealTime {
    private long id;

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