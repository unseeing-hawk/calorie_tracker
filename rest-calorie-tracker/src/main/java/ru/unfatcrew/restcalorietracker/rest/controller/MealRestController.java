package ru.unfatcrew.restcalorietracker.rest.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import ru.unfatcrew.restcalorietracker.pojo.entity.MealTime;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.unfatcrew.restcalorietracker.pojo.dto.MealGetDto;
import ru.unfatcrew.restcalorietracker.pojo.dto.MealPostDto;
import ru.unfatcrew.restcalorietracker.pojo.request.ChangeMealsRequest;
import ru.unfatcrew.restcalorietracker.pojo.response.ChangeMealsResponse;
import ru.unfatcrew.restcalorietracker.service.MealService;
import ru.unfatcrew.restcalorietracker.validation.annotation.LessThan10YearOldDate;

import java.util.List;

@Validated
@RestController
@RequestMapping("/meals")
public class MealRestController {

    private final MealService mealService;

    @Autowired
    public MealRestController(MealService mealService) {
        this.mealService = mealService;
    }

    @PostMapping
    public MealGetDto addMeals(@RequestBody MealPostDto mealPostDto) {
        return mealService.addMeals(mealPostDto);
    }

    @PutMapping
    public ChangeMealsResponse changeMeals(@RequestBody ChangeMealsRequest changeMealsRequest) {
        return mealService.changeMeals(changeMealsRequest);
    }

    @GetMapping("/mealtimes")
    public List<MealTime> getMealTimes() {
        return mealService.getMealTimes();
    }

    @GetMapping
    public MealGetDto getMeals(@RequestParam(name="date", defaultValue="")
                                   @Valid
                                   @Size(min=10, max=10)
                                   @LessThan10YearOldDate
                                   String date,
                               @RequestParam(name="user-login", defaultValue="")
                                   @Valid
                                   @Size(min=8, max=30)
                                   String userLogin) {
        return mealService.getMeals(date, userLogin);
    }
}
