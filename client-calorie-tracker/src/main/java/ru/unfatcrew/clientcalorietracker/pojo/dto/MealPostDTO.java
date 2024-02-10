package ru.unfatcrew.clientcalorietracker.pojo.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public class MealPostDTO {
    @NotEmpty
    private List<@Valid MealPostDataDTO> mealPostDataList;

    @Size(min=8, max=30)
    private String userLogin;

    @Size(min=10, max=10)
    private String date;

    @NotBlank
    private String mealTime;

    public MealPostDTO() {

    }

    public MealPostDTO(List<MealPostDataDTO> mealPostDataList, String userLogin, String date, String mealTime) {
        this.mealPostDataList = mealPostDataList;
        this.userLogin = userLogin;
        this.date = date;
        this.mealTime = mealTime;
    }

    public MealPostDTO(List<MealPostDataDTO> mealPostDataList, String date, String mealTime) {
        this.mealPostDataList = mealPostDataList;
        this.userLogin = "";
        this.date = date;
        this.mealTime = mealTime;
    }

    public List<MealPostDataDTO> getMealPostDataList() {
        return mealPostDataList;
    }

    public void setMealPostDataList(List<MealPostDataDTO> mealPostDataList) {
        this.mealPostDataList = mealPostDataList;
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
