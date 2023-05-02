package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ModelNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {

    private UserController controller;
    private User user;
    private static Validator validator;

    @BeforeAll
    public static void validatorInit() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeEach
    public void init() {
        controller = new UserController(new UserService(new InMemoryUserStorage()));
        user = User.builder()
                .email("email@email.ru")
                .login("Login")
                .name("Name")
                .birthday(LocalDate.of(2000, 10, 10))
                .build();
    }

    @Test
    public void shouldReturnListWIthUsers() {
        User user2 = User.builder()
                .email("email2@email.ru")
                .login("Login2")
                .name("Name2")
                .birthday(LocalDate.of(2002, 10, 10))
                .build();
        User user3 = User.builder()
                .email("email3@email.ru")
                .login("Login3")
                .name("Name3")
                .birthday(LocalDate.of(2003, 10, 10))
                .build();
        assertTrue(controller.findAll().isEmpty());
        controller.create(user);
        assertEquals(1, controller.findAll().size());
        controller.create(user2);
        assertEquals(2, controller.findAll().size());
        controller.create(user3);
        assertEquals(3, controller.findAll().size());
        controller.create(user);
        assertEquals(3, controller.findAll().size());
    }

    @Test
    public void shouldCreateUserWithParametersAndSetId() {
        User actualUser = controller.create(user);
        assertEquals(1, actualUser.getId());
        assertEquals("Login", actualUser.getLogin());
        assertEquals("Name", actualUser.getName());
        assertEquals("email@email.ru", actualUser.getEmail());
        assertEquals(LocalDate.of(2000, 10, 10), actualUser.getBirthday());
        assertFalse(controller.findAll().isEmpty());
    }

    @Test
    public void shouldSetNameWithLoginIfNoNameGiven() {
        user = user.toBuilder().name("").build();
        User actualUser = controller.create(user);
        assertEquals("Login", actualUser.getName());
    }

    @Test
    public void shouldUpdateUserWithParameters() {
        controller.create(user);
        User updatedUser = User.builder()
                .id(1)
                .email("updatedEmail@email.ru")
                .login("UpdatedLogin")
                .name("UpdatedName")
                .birthday(LocalDate.of(2002, 10, 10))
                .build();
        User actualUser = controller.update(updatedUser);
        assertEquals(1, actualUser.getId());
        assertEquals("UpdatedLogin", actualUser.getLogin());
        assertEquals("UpdatedName", actualUser.getName());
        assertEquals("updatedEmail@email.ru", actualUser.getEmail());
        assertEquals(LocalDate.of(2002, 10, 10), actualUser.getBirthday());
        assertFalse(controller.findAll().isEmpty());
    }

    @Test
    public void testEmailValidation() {
        user = user.toBuilder().email("definitely not email").build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertEquals("Введенная строка не является email!", violations.iterator().next().getMessage());
    }

    @Test
    public void testEmailNotNullValidation() {
        user = user.toBuilder().email(null).build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertEquals("Email не может быть null!", violations.iterator().next().getMessage());
    }

    @Test
    public void testLoginValidation() {
        user = user.toBuilder().login("").build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertEquals("Логин не может быть пустым!", violations.iterator().next().getMessage());
    }

    @Test
    public void testBirthdayValidation() {
        user = user.toBuilder().birthday(LocalDate.of(2077, 10, 10)).build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertEquals("День рождения не может быть в будущем!", violations.iterator().next().getMessage());
    }

    @Test
    public void shouldThrowValidationExceptionIfNoUserToUpdate() {
        user = user.toBuilder().id(1).build();
        ModelNotFoundException e = assertThrows(ModelNotFoundException.class, () -> controller.update(user));
        assertEquals(e.getMessage(), "Пользователь с id 1 отсутствует!");
    }
}
