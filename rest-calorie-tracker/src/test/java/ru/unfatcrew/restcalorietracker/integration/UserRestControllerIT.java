package ru.unfatcrew.restcalorietracker.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.unfatcrew.restcalorietracker.dao.UserDAO;
import ru.unfatcrew.restcalorietracker.pojo.entity.User;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserRestControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserDAO userDAO;

    private User user;

    @BeforeEach
    private void setup() {
        userDAO.deleteAll();

        user = new User("Beer Master",
                "beermastergood",
                "{bcrypt}$2a$10$RSvT7b55.bwDLBg4c1Rw/uWygwjfvxFw.MJo/ZlRDnEr1xSwPkJU2",
                49.54f);
    }

    @DisplayName("Create user with invalid request body")
    @Test
    public void givenInvalidUserRequestBody_whenCreateUser_thenThrowException() throws Exception {
        user.setWeight(-6);

        ResultActions response = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)));

        response.andExpect(status().isBadRequest());
    }

    @DisplayName("Create user with already existing login")
    @Test
    public void givenUserRequestBodyWithExistingLogin_whenCreateUser_thenThrowException() throws Exception {
        userDAO.save(user);
        ResultActions response = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)));

        response.andExpect(status().isBadRequest());
    }

    @DisplayName("Create user")
    @Test
    public void givenUserRequestBody_whenCreateUser_thenReturnUserObject() throws Exception {
        ResultActions response = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.login", is(user.getLogin())))
                .andExpect(jsonPath("$.password", is(user.getPassword())))
                .andExpect(jsonPath("$.weight", Float.class).value(user.getWeight()));
    }

    @DisplayName("Find user with invalid login")
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"ds", "1234567890123456789012345678901"})
    public void givenInvalidLogin_whenFindUser_thenThrowException(String login) throws Exception {
        ResultActions response = mockMvc.perform(get("/users").param("login", login));

        response.andExpect(status().isBadRequest());
    }

    @DisplayName("Find user with not existing login")
    @Test
    public void givenNotExistingLogin_whenFindUser_thenThrowException() throws Exception{
        ResultActions response = mockMvc.perform(get("/users").param("login", user.getName()));

        response.andExpect(status().isNotFound());
    }

    @DisplayName("Find user")
    @ParameterizedTest
    @ValueSource(strings = {"12345678", "123456789012345678901234567890"})
    public void givenLogin_whenFindUser_thenReturnUserObject(String login) throws Exception {
        user.setLogin(login);
        userDAO.save(user);

        ResultActions response = mockMvc.perform(get("/users").param("login", user.getLogin()));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.login", is(user.getLogin())));
    }

    @DisplayName("Update user with invalid request body")
    @Test
    public void givenInvalidUserRequestBody_whenUpdateUser_thenThrowException() throws Exception {
        userDAO.save(user);
        user.setWeight(-8);

        ResultActions response = mockMvc.perform(put("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)));

        response.andExpect(status().isBadRequest());
    }

    @DisplayName("Update user with not existing id or login")
    @Test
    public void givenUserRequestBodyWithNotExistingIdOrLogin_whenUpdateUser_thenThrowException() throws Exception {
        userDAO.save(user);
        user.setLogin("asdfasdf");

        ResultActions response = mockMvc.perform(put("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)));

        response.andExpect(status().isNotFound());
    }

    @DisplayName("Update user")
    @Test
    public void givenUserRequestBody_whenUpdateUser_thenReturnUserObject() throws Exception {
        userDAO.save(user);
        user.setName("Alex Matatabi");
        user.setWeight(56.47f);

        ResultActions response = mockMvc.perform(put("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.login", is(user.getLogin())))
                .andExpect(jsonPath("$.password", is(user.getPassword())))
                .andExpect(jsonPath("$.weight", Float.class).value(user.getWeight()));
    }
}
