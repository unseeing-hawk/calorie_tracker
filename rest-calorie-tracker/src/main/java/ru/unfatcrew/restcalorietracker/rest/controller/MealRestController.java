package ru.unfatcrew.restcalorietracker.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.unfatcrew.restcalorietracker.pojo.dto.MealGetDto;
import ru.unfatcrew.restcalorietracker.pojo.dto.MealPostDto;
import ru.unfatcrew.restcalorietracker.service.MealService;

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
}
