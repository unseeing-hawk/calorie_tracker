package ru.unfatcrew.restcalorietracker.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.unfatcrew.restcalorietracker.dao.MealDAO;
import ru.unfatcrew.restcalorietracker.dao.MealTimeDAO;
import ru.unfatcrew.restcalorietracker.dao.ProductDAO;
import ru.unfatcrew.restcalorietracker.dao.UserDAO;
import ru.unfatcrew.restcalorietracker.pojo.dto.MealPostDTO;
import ru.unfatcrew.restcalorietracker.pojo.entity.Meal;
import ru.unfatcrew.restcalorietracker.pojo.entity.MealTime;
import ru.unfatcrew.restcalorietracker.pojo.entity.Product;
import ru.unfatcrew.restcalorietracker.pojo.entity.User;
import ru.unfatcrew.restcalorietracker.rest.exception_handling.exception.IllegalRequestArgumentException;
import ru.unfatcrew.restcalorietracker.rest.exception_handling.exception.ResourceNotFoundException;
import ru.unfatcrew.restcalorietracker.rest.exception_handling.validation.Violation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Validated
@Service
public class MealService {

    public static final DateTimeFormatter DateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private final UserDAO userDAO;
    private final ProductDAO productDAO;
    private final MealDAO mealDAO;
    private final MealTimeDAO mealTimeDAO;

    @Autowired
    public MealService(UserDAO userDAO, ProductDAO productDAO, MealDAO mealDAO, MealTimeDAO mealTimeDAO) {
        this.userDAO = userDAO;
        this.productDAO = productDAO;
        this.mealDAO = mealDAO;
        this.mealTimeDAO = mealTimeDAO;
    }

    public List<MealTime> getMealTimes() {
        return mealTimeDAO.findAll();
    }

    @Transactional
    public List<MealPostDTO> createMeals(@Valid @NotNull @NotEmpty List<MealPostDTO> mealPostDTOList) {
        List<Violation> violationList = new ArrayList<>();
        List<Meal> mealList = new ArrayList<>();
        for (int i = 0; i < mealPostDTOList.size(); i++) {
            if (mealPostDTOList.get(i).getId() != 0) {
                violationList.add(new Violation("createMeals.mealPostDTOList["
                        + Integer.toString(i)
                        + "].id",
                        "must be 0"));
            }

            mealList.add(validateMealPostDtoFieldsExistence(mealPostDTOList.get(i), violationList, i));
        }

        if (!violationList.isEmpty()) {
            throw new ResourceNotFoundException(violationList);
        }

        List<MealPostDTO> returnableMealPostDTOList = new ArrayList<>();
        for (Meal meal : mealList) {
            mealDAO.save(meal);
            returnableMealPostDTOList.add(new MealPostDTO(meal));
        }

        return returnableMealPostDTOList;
    }

    public Meal validateMealPostDtoFieldsExistence(MealPostDTO mealPostDTO,
                                                   List<Violation> violationList,
                                                   int index) {
        User user = userDAO.findByLogin(mealPostDTO.getUserLogin());
        if (user == null) {
            violationList.add(new Violation("createMeals.mealPostDTOList["
                    + Integer.toString(index)
                    + "].userLogin",
                    "not found"));
        }

        Optional<Product> optionalProduct = productDAO.findById(mealPostDTO.getProductId());
        Product product = null;
        if (optionalProduct.isEmpty()) {
            violationList.add(new Violation("createMeals.mealPostDTOList["
                    + Integer.toString(index)
                    + "].productId",
                    "not found"));
        } else {
            product = optionalProduct.get();
        }

        MealTime mealTime = mealTimeDAO.findByName(mealPostDTO.getMealTime());
        if (mealTime == null) {
            violationList.add(new Violation("createMeals.mealPostDTOList["
                    + Integer.toString(index)
                    + "].mealTime",
                    "not found"));
        }

        if (product != null && !product.isActive()) {
            violationList.add(new Violation("createMeals.mealPostDTOList["
                    + Integer.toString(index)
                    + "].productId",
                    "is not active"));
        }

        if (product != null
                && user != null
                && product.getUser().getId() != user.getId()) {
            violationList.add(new Violation("createMeals.mealPostDTOList["
                    + Integer.toString(index)
                    + "].productId-userLogin",
                    "are not connected"));
        }
        
        return violationList.isEmpty()
                ? new Meal(user,
                        product,
                        mealPostDTO.getWeight(),
                        LocalDate.parse(mealPostDTO.getDate(), DateFormat),
                        mealTime)
                : new Meal();
    }
}
