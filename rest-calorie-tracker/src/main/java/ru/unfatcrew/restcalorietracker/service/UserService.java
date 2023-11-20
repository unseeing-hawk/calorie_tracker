package ru.unfatcrew.restcalorietracker.service;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.unfatcrew.restcalorietracker.dao.UserDAO;
import ru.unfatcrew.restcalorietracker.pojo.entity.User;

@Validated
@Service
public class UserService {

    private final UserDAO userDAO;

    @Autowired
    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Transactional
    public User createUser(@Valid User user) {
        return user;
    }
}
