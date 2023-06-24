package ru.yandex.practicum.filmorate.validation;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class UserValidationTest {

    private User user;
    private static Validator validator;

    @BeforeAll
    public static void validatorInit() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeEach
    public void init() {
        user = User.builder()
                .email("email@email.ru")
                .login("Login")
                .name("Name")
                .birthday(LocalDate.of(2000, 10, 10))
                .build();
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
}
