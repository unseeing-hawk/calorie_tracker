package ru.unfatcrew.restcalorietracker.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.unfatcrew.restcalorietracker.pojo.entity.User;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserDAOTests {

    @Autowired
    private UserDAO userDAO;

    private User user;

    @BeforeEach
    public void setup() {
        user = new User("Beer Master",
                "beermastergood",
                "{bcrypt}$2a$10$RSvT7b55.bwDLBg4c1Rw/uWygwjfvxFw.MJo/ZlRDnEr1xSwPkJU2",
                49.54f);
    }

    @DisplayName("Save user correctly")
    @Test
    public void givenUserObject_whenSaveUser_thenReturnUserObject() {
        User savedUser = userDAO.save(user);

        assertAll("saved user",
                () -> assertNotNull(savedUser),
                () -> assertTrue(savedUser.getId() > 0));
    }

    @DisplayName("Update user correctly")
    @Test
    public void givenUserObject_whenUpdateUser_thenReturnUpdatedUserObject() {
        String newName = "Laura Palmer";
        String newPassword = "{bcrypt}$2a$10$RSvT7b55.bwDLBg4c1Rw/uWygwjfvxFw.MJo/ZlRDnEr1xSwPk890";
        float newWeight = 54.67f;

        User savedUser = userDAO.save(user);
        savedUser.setName(newName);
        savedUser.setPassword(newPassword);
        savedUser.setWeight(newWeight);
        User updatedUser = userDAO.save(savedUser);

        assertAll("updated user",
                () -> assertEquals(newName, updatedUser.getName()),
                () -> assertEquals(newPassword, updatedUser.getPassword()),
                () -> assertEquals(newWeight, updatedUser.getWeight()));
    }

    @DisplayName("Find by login which exists")
    @Test
    public void givenLogin_whenFindByLogin_thenReturnUserObject() {
        User savedUser = userDAO.save(user);
        User foundUser = userDAO.findByLogin(savedUser.getLogin());

        assertNotNull(foundUser);
    }

    @DisplayName("Find by login which does not exist")
    @Test
    public void givenLogin_whenFindByNotExistingLogin_thenReturnNull() {
        String notExistingLogin = "fesfesfe";

        User savedUser = userDAO.save(user);
        User foundUser = userDAO.findByLogin(notExistingLogin);

        assertNull(foundUser);
    }

    @DisplayName("Find by both existing id and login")
    @Test
    public void givenIdAndLogin_whenFindByIdAndLogin_thenReturnUserObject() {
        User savedUser = userDAO.save(user);
        User foundUser = userDAO.findByIdAndLogin(savedUser.getId(), savedUser.getLogin());

        assertNotNull(foundUser);
    }

    @DisplayName("Find by existing id and not existing login")
    @Test
    public void givenExistingIdAndNotExistingLogin_whenFindByIdAndLogin_thenReturnNull() {
        String notExistingLogin = "fefefe";

        User savedUser = userDAO.save(user);
        User foundUser = userDAO.findByIdAndLogin(savedUser.getId(), notExistingLogin);

        assertNull(foundUser);
    }

    @DisplayName("Find by not existing id and existing login")
    @Test
    public void givenNotExistingIdAndExistingLogin_whenFindByIdAndLogin_thenReturnNull() {
        User savedUser = userDAO.save(user);
        long notExistingId = savedUser.getId() + 1;
        User foundUser = userDAO.findByIdAndLogin(notExistingId, savedUser.getLogin());

        assertNull(foundUser);
    }

    @DisplayName("Find by not existing id and not existing login")
    @Test
    public void givenNotExistingIdAndNotExistingLogin_whenFindByIdAndLogin_thenReturnNull() {
        String notExistingLogin = "fefefe";

        User savedUser = userDAO.save(user);
        long notExistingId = savedUser.getId() + 1;
        User foundUser = userDAO.findByIdAndLogin(notExistingId, notExistingLogin);

        assertNull(foundUser);
    }
}
