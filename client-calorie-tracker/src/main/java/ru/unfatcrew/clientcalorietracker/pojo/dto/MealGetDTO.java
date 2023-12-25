package ru.unfatcrew.clientcalorietracker.pojo.dto;

import java.util.List;

public class MealGetDTO {

    private List<MealGetDataDTO> mealGetDataList;

    private String userLogin;

    private String date;

    public MealGetDTO() {

    }

    public MealGetDTO(List<MealGetDataDTO> mealGetDataList, String userLogin, String date) {
        this.mealGetDataList = mealGetDataList;
        this.userLogin = userLogin;
        this.date = date;
    }

    public List<MealGetDataDTO> getMealGetDataList() {
        return mealGetDataList;
    }

    public void setMealGetDataList(List<MealGetDataDTO> mealGetDataList) {
        this.mealGetDataList = mealGetDataList;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}