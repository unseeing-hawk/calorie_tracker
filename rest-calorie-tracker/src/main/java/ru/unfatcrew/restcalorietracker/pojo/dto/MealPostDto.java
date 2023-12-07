package ru.unfatcrew.restcalorietracker.pojo.dto;

import java.util.List;

public class MealPostDto {

    private List<MealProductDataDto> mealProductDataList;

    private String userLogin;

    private String date;

    private String mealTime;

    public MealPostDto() {

    }

    public MealPostDto(List<MealProductDataDto> mealProductDataList, String userLogin, String date, String mealTime) {
        this.mealProductDataList = mealProductDataList;
        this.userLogin = userLogin;
        this.date = date;
        this.mealTime = mealTime;
    }

    public List<MealProductDataDto> getMealProductDataList() {
        return mealProductDataList;
    }

    public void setMealProductDataList(List<MealProductDataDto> mealProductDataList) {
        this.mealProductDataList = mealProductDataList;
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

    public String getMealTime() {
        return mealTime;
    }

    public void setMealTime(String mealTime) {
        this.mealTime = mealTime;
    }
}
