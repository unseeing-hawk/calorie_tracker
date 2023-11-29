package ru.unfatcrew.restcalorietracker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.unfatcrew.restcalorietracker.dao.MealDAO;
import ru.unfatcrew.restcalorietracker.dao.MealTimeDAO;
import ru.unfatcrew.restcalorietracker.pojo.entity.MealTime;

import java.util.List;

@Service
public class MealService {

    private final MealDAO mealDAO;
    private final MealTimeDAO mealTimeDAO;

    @Autowired
    public MealService(MealDAO mealDAO, MealTimeDAO mealTimeDAO) {
        this.mealDAO = mealDAO;
        this.mealTimeDAO = mealTimeDAO;
    }

    public List<MealTime> getMealTimes() {
        return mealTimeDAO.findAll();
    }
}
