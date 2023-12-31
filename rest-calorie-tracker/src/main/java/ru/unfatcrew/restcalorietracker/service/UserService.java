package ru.unfatcrew.restcalorietracker.service;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.unfatcrew.restcalorietracker.dao.UserDAO;
import ru.unfatcrew.restcalorietracker.pojo.entity.User;
import ru.unfatcrew.restcalorietracker.rest.exception_handling.exception.IllegalRequestArgumentException;
import ru.unfatcrew.restcalorietracker.rest.exception_handling.exception.ResourceNotFoundException;
import ru.unfatcrew.restcalorietracker.rest.exception_handling.validation.Violation;

import java.util.ArrayList;
import java.util.List;

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
        List<Violation> violationList = new ArrayList<>();
        if (user.getId() != 0) {
            violationList.add(new Violation("createUser.user.id", "must be 0"));
        }

        User userWithProbablySameLogin = userDAO.findByLogin(user.getLogin());
        if (userWithProbablySameLogin != null) {
            violationList.add(new Violation("createUser.user.login", "already exists"));
        }

        if (!violationList.isEmpty()) {
            throw new IllegalRequestArgumentException(violationList);
        }

        userDAO.save(user);

        return user;
    }

    public User findUser(String login) {
        List<Violation> violationList = new ArrayList<>();
        User user = userDAO.findByLogin(login);
        if (user == null) {
            violationList.add(new Violation("findUser.login", "not found"));
            throw new ResourceNotFoundException(violationList);
        }

        return user;
    }

    @Transactional
    public User updateUser(@Valid User user) {
        List<Violation> violationList = new ArrayList<>();
        if (user.getId() <= 0) {
            violationList.add(new Violation("updateUser.user.id", "must be positive"));
            throw new IllegalRequestArgumentException(violationList);
        }

        User userForUpdate = userDAO.findByIdAndLogin(user.getId(), user.getLogin());
        if (userForUpdate == null) {
            violationList.add(new Violation("updateUser.user.id-login", "not found"));
            throw new ResourceNotFoundException(violationList);
        }

        userDAO.save(user);

        return user;
    }
}
