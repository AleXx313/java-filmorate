package ru.yandex.practicum.filmorate.validation;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilmValidationTest {

    private Film film;
    private static Validator validator;

    @BeforeAll
    public static void validatorInit() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeEach
    void init() {
        film = Film.builder()
                .name("Film")
                .description("Film description")
                .releaseDate(LocalDate.of(1990, 10, 10))
                .duration(120)
                .build();
    }

    @Test
    public void testNameValidation() {
        film = film.toBuilder().name(" ").build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
        assertEquals("Название фильма не может быть пустым!", violations.iterator().next().getMessage());
    }

    @Test
    public void testDescriptionValidation() {
        film = film.toBuilder()
                .description("|||||||<Очень длинное описание!>|||||||" +
                        "|||||||<Очень длинное описание!>|||||||" +
                        "|||||||<Очень длинное описание!>|||||||" +
                        "|||||||<Очень длинное описание!>|||||||" +
                        "|||||||<Очень длинное описание!>|||||||" +
                        "|||||||<Очень длинное описание!>|||||||")
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
        assertEquals("Описание фильма не должно превышать 200 символов!",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testReleaseDateValidation() {
        film = film.toBuilder().releaseDate(LocalDate.of(1000, 10, 10)).build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
        assertEquals("Дата релиза указана неверно!", violations.iterator().next().getMessage());
    }

    @Test
    public void testDurationValidation() {
        film = film.toBuilder().duration(-10).build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
        assertEquals("Длительность фильма не может быть меньше или равна нулю!",
                violations.iterator().next().getMessage());
    }
}
