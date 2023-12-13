package ru.unfatcrew.restcalorietracker.pojo.dto;

import java.util.List;

public class MealGetDto {

    private List<MealGetDataDto> mealGetDataList;

    private String userLogin;

    private String date;

    public MealGetDto() {

    }

    public MealGetDto(List<MealGetDataDto> mealGetDataList, String userLogin, String date) {
        this.mealGetDataList = mealGetDataList;
        this.userLogin = userLogin;
        this.date = date;
    }

    public List<MealGetDataDto> getMealGetDataList() {
        return mealGetDataList;
    }

    public void setMealGetDataList(List<MealGetDataDto> mealGetDataList) {
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
