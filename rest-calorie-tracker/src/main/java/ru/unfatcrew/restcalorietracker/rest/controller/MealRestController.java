package ru.unfatcrew.restcalorietracker.rest.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.unfatcrew.restcalorietracker.pojo.dto.MealPostDTO;
import ru.unfatcrew.restcalorietracker.pojo.entity.MealTime;
import ru.unfatcrew.restcalorietracker.service.MealService;

import java.util.List;


@RestController
@RequestMapping("/meals")
public class MealRestController {

    private final MealService mealService;

    @Autowired
    public MealRestController(MealService mealService) {
        this.mealService = mealService;
    }

    @GetMapping("/mealtimes")
    public List<MealTime> getMealTimes() {
        return mealService.getMealTimes();
    }

    @PostMapping
    public List<MealPostDTO> createMeals(@RequestBody List<MealPostDTO> mealPostDTOList) {
        return mealService.createMeals(mealPostDTOList);
    }
}
