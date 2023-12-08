package ru.unfatcrew.restcalorietracker.pojo.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;
import ru.unfatcrew.restcalorietracker.validation.annotation.LessThan10YearOldDate;

import java.util.List;

public class MealPostDto {

    @NotEmpty
    private List<@Valid MealPostDataDto> mealPostDataList;

    @Size(min=8, max=30)
    private String userLogin;

    @Size(min=10, max=10)
    @LessThan10YearOldDate
    private String date;

    @NotBlank
    private String mealTime;

    public MealPostDto() {

    }

    public MealPostDto(List<MealPostDataDto> mealPostDataList, String userLogin, String date, String mealTime) {
        this.mealPostDataList = mealPostDataList;
        this.userLogin = userLogin;
        this.date = date;
        this.mealTime = mealTime;
    }

    public List<MealPostDataDto> getMealPostDataList() {
        return mealPostDataList;
    }

    public void setMealPostDataList(List<MealPostDataDto> mealPostDataList) {
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
