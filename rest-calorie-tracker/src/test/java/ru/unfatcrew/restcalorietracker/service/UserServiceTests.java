package ru.unfatcrew.restcalorietracker.service;

import jakarta.validation.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.unfatcrew.restcalorietracker.dao.UserDAO;
import ru.unfatcrew.restcalorietracker.pojo.entity.User;
import ru.unfatcrew.restcalorietracker.rest.exception_handling.exception.IllegalRequestArgumentException;
import ru.unfatcrew.restcalorietracker.rest.exception_handling.exception.ResourceNotFoundException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserDAO userDAO;

    private User user;

    private static Validator validator;

    @BeforeAll
    public static void setupValidator() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @BeforeEach
    public void setupUser() {
        user = new User("Beer Master",
                "beermastergood",
                "{bcrypt}$2a$10$RSvT7b55.bwDLBg4c1Rw/uWygwjfvxFw.MJo/ZlRDnEr1xSwPkJU2",
                49.54f);
    }

    @DisplayName("Validate valid user object")
    @ParameterizedTest
    @CsvFileSource(resources="/valid-user-pairwise.csv",
            numLinesToSkip=1,
            delimiter=';')
    public void givenValidUserObject_whenValidate_thenNothing(String name,
                                                              String login,
                                                              String password,
                                                              String weight) {
        float floatWeight = weight.equals("MAX")
                ? Float.MAX_VALUE
                : Float.parseFloat(weight.replace(",", "."));
        User validUser = new User(name, login, password, floatWeight);

        Set<ConstraintViolation<User>> violations = validator.validate(validUser);

        assertTrue(violations.isEmpty());
    }

    @DisplayName("Validate invalid user object")
    @ParameterizedTest
    @CsvFileSource(resources="/invalid-user.csv",
            numLinesToSkip=1,
            delimiter=';',
            nullValues={"NULL"})
    public void givenInvalidUserObject_whenValidate_thenReturnViolations(String name,
                                                                         String login,
                                                                         String password,
                                                                         String weight) {
        User invalidUser = new User(name, login, password, Float.parseFloat(weight.replace(",", ".")));

        Set<ConstraintViolation<User>> violations = validator.validate(invalidUser);

        assertFalse(violations.isEmpty());
    }

    @DisplayName("Create user with invalid id")
    @ParameterizedTest
    @ValueSource(longs = {-5, 6})
    public void givenUserObjectWithInvalidId_whenCreateUser_thenThrowException(long id) {
        user.setId(id);

        assertThrows(IllegalRequestArgumentException.class, () -> {
            userService.createUser(user);
        });

        verify(userDAO, never()).save(any(User.class));
    }

    @DisplayName("Create user with existing login")
    @Test
    public void givenUserObjectWithExistingLogin_whenCreateUser_thenThrowException() {
        given(userDAO.findByLogin(user.getLogin())).willReturn(user);

        assertThrows(IllegalRequestArgumentException.class, () -> {
            userService.createUser(user);
        });

        verify(userDAO, never()).save(any(User.class));
    }

    @DisplayName("Create user")
    @Test
    public void givenUserObject_whenCreateUser_thenReturnUserObject() {
        given(userDAO.findByLogin(user.getLogin())).willReturn(null);
        given(userDAO.save(user)).willReturn(user);

        User savedUser = userService.createUser(user);

        assertNotNull(savedUser);
    }

    @DisplayName("Find user with login that does not exist")
    @Test
    public void givenLoginThatDoesNotExist_whenFindUser_thenThrowException() {
        given(userDAO.findByLogin(user.getLogin())).willReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> {
            userService.findUser(user.getLogin());
        });
    }

    @DisplayName("Find user")
    @Test
    public void givenLogin_whenFindUser_thenReturnUserObject() {
        given(userDAO.findByLogin(user.getLogin())).willReturn(user);

        User foundUser = userService.findUser(user.getLogin());

        assertEquals(user.getLogin(), foundUser.getLogin());
    }

    @DisplayName("Update user with invalid id")
    @ParameterizedTest
    @ValueSource(longs = {0, -7})
    public void givenUserObjectWithInvalidId_whenUpdateUser_thenThrowException(long id) {
        user.setId(id);

        assertThrows(IllegalRequestArgumentException.class, () -> {
            userService.updateUser(user);
        });

        verify(userDAO, never()).save(any(User.class));
    }

    @DisplayName("Update user where id or login do not exist")
    @Test
    public void givenUserObjectWithNotExistingIdOrLogin_whenUpdateUser_thenThrowException() {
        user.setId(5);
        given(userDAO.findByIdAndLogin(user.getId(), user.getLogin())).willReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> {
            userService.updateUser(user);
        });

        verify(userDAO, never()).save(any(User.class));
    }

    @DisplayName("Update user")
    @Test
    public void givenUserObject_whenUpdateUser_thenReturnUserObject() {
        User oldUser = new User("Ded Nikifor",
                "beermastergood",
                "{bcrypt}$2a$10$RSvT7b55.bwDLBg4c1Rw/uWygwjfvxFw.MJo/ZlRDnEr1xSwPkJU2",
                35.4f);
        oldUser.setId(5);
        user.setId(5);
        given(userDAO.findByIdAndLogin(user.getId(), user.getLogin())).willReturn(oldUser);
        given(userDAO.save(user)).willReturn(user);

        User updatedUser = userService.updateUser(user);

        assertAll("updated user",
                () -> {
                    assertNotNull(updatedUser);
                },
                () -> {
                    assertEquals(user.getId(), updatedUser.getId());
                },
                () -> {
                    assertEquals(user.getName(), updatedUser.getName());
                },
                () -> {
                    assertEquals(user.getLogin(), updatedUser.getLogin());
                },
                () -> {
                    assertEquals(user.getPassword(), updatedUser.getPassword());
                },
                () -> {
                    assertEquals(user.getWeight(), updatedUser.getWeight());
                });
    }
}
