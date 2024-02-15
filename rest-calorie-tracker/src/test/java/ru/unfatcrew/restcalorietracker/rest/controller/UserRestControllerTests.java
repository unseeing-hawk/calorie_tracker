package ru.unfatcrew.restcalorietracker.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.unfatcrew.restcalorietracker.pojo.entity.User;
import ru.unfatcrew.restcalorietracker.rest.exception_handling.exception.IllegalRequestArgumentException;
import ru.unfatcrew.restcalorietracker.rest.exception_handling.exception.ResourceNotFoundException;
import ru.unfatcrew.restcalorietracker.service.UserService;

import java.util.ArrayList;
import java.util.HashSet;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.hamcrest.CoreMatchers.is;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(UserRestController.class)
public class UserRestControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;

    @BeforeEach
    public void setupUser() {
        user = new User("Beer Master",
                "beermastergood",
                "{bcrypt}$2a$10$RSvT7b55.bwDLBg4c1Rw/uWygwjfvxFw.MJo/ZlRDnEr1xSwPkJU2",
                49.54f);
    }

    @DisplayName("Create user with invalid request body")
    @Test
    public void givenInvalidUserRequestBody_whenCreateUser_thenThrowExceptions() throws Exception {
        given(userService.createUser(any(User.class))).willThrow(new ConstraintViolationException(new HashSet<>()));

        ResultActions response = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)));

        response.andExpect(status().isBadRequest());
    }

    @DisplayName("Create user with validated but invalid request body")
    @Test
    public void givenValidatedButInvalidUserRequestBody_whenCreateUser_thenThrowException() throws Exception {
        given(userService.createUser(any(User.class)))
                .willThrow(new IllegalRequestArgumentException(new ArrayList<>()));

        ResultActions response = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)));

        response.andExpect(status().isBadRequest());
    }

    @DisplayName("Create user")
    @Test
    public void givenUserRequestBody_whenCreateUser_thenReturnUserObject() throws Exception {
        given(userService.createUser(any(User.class))).willAnswer((invocation) -> invocation.getArgument(0));

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
        given(userService.findUser(any(String.class))).willThrow(new ResourceNotFoundException(new ArrayList<>()));

        ResultActions response = mockMvc.perform(get("/users").param("login", user.getName()));

        response.andExpect(status().isNotFound());
    }

    @DisplayName("Find user")
    @ParameterizedTest
    @ValueSource(strings = {"12345678", "123456789012345678901234567890"})
    public void givenLogin_whenFindUser_thenReturnUserObject(String login) throws Exception {
        user.setLogin(login);
        given(userService.findUser(user.getLogin())).willReturn(user);

        ResultActions response = mockMvc.perform(get("/users").param("login", user.getLogin()));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.login", is(user.getLogin())));
    }

    @DisplayName("Update user with invalid request body")
    @Test
    public void givenInvalidUserRequestBody_whenUpdateUser_thenThrowException() throws Exception {
        given(userService.updateUser(any(User.class))).willThrow(new ConstraintViolationException(new HashSet<>()));

        ResultActions response = mockMvc.perform(put("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)));

        response.andExpect(status().isBadRequest());
    }

    @DisplayName("Update user with validated but invalid request body")
    @Test
    public void givenValidatedButInvalidUserRequestBody_whenUpdateUser_thenThrowException() throws Exception {
        given(userService.updateUser(any(User.class))).willThrow(new IllegalRequestArgumentException(new ArrayList<>()));

        ResultActions response = mockMvc.perform(put("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)));

        response.andExpect(status().isBadRequest());
    }

    @DisplayName("Update user with not existing id or login")
    @Test
    public void givenUserRequestBodyWithNotExistingIdOrLogin_whenUpdateUser_thenThrowException() throws Exception {
        given(userService.updateUser(any(User.class))).willThrow(new ResourceNotFoundException(new ArrayList<>()));

        ResultActions response = mockMvc.perform(put("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)));

        response.andExpect(status().isNotFound());
    }

    @DisplayName("Update user")
    @Test
    public void givenUserRequestBody_whenUpdateUser_thenReturnUserObject() throws Exception {
        given(userService.updateUser(any(User.class))).willAnswer((invocation) -> invocation.getArgument(0));

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
