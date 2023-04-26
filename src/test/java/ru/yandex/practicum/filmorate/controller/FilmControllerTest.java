package ru.yandex.practicum.filmorate.controller;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


public class FilmControllerTest {

    private FilmController controller;
    private Film film;
    private static Validator validator;

    @BeforeAll
    public static void validatorInit() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeEach
    void init() {
        controller = new FilmController();
        film = Film.builder()
                .name("Film")
                .description("Film description")
                .releaseDate(LocalDate.of(1990, 10, 10))
                .duration(120)
                .build();
    }

    @Test
    public void shouldReturnListWithFilms() {
        Film film2 = Film.builder()
                .name("Film2")
                .description("Film2 description")
                .releaseDate(LocalDate.of(1989, 10, 10))
                .duration(125)
                .build();
        Film film3 = Film.builder()
                .name("Film3")
                .description("Film3 description")
                .releaseDate(LocalDate.of(1988, 10, 10))
                .duration(115)
                .build();
        assertTrue(controller.findAll().isEmpty());
        controller.create(film);
        assertEquals(1, controller.findAll().size());
        controller.create(film2);
        assertEquals(2, controller.findAll().size());
        controller.create(film3);
        assertEquals(3, controller.findAll().size());
        controller.create(film);
        assertEquals(3, controller.findAll().size());
    }

    @Test
    public void shouldCreateFilmFromParametersAndSetId() {
        Film actualFilm = controller.create(film);
        assertEquals(1, actualFilm.getId(), "ID не соответствует ожидаемому.");
        assertEquals("Film", actualFilm.getName(), "Имя не соответствует ожидаемому.");
        assertEquals("Film description", actualFilm.getDescription(),
                "Описание не соответствует ожидаемому.");
        assertEquals(LocalDate.of(1990, 10, 10), actualFilm.getReleaseDate(),
                "Дата релиза не соответствует ожидаемой.");
        assertEquals(120, actualFilm.getDuration());
        assertEquals(controller.findAll().get(0), actualFilm, "Сохраненный фильм не соответствует ожидаемому!");
        assertFalse(controller.findAll().isEmpty());
    }

    @Test
    public void shouldUpdateFilmFromParameters() {
        controller.create(film);
        Film updatedFilm = Film.builder()
                .id(1)
                .name("UpdatedFilm")
                .description("UpdatedFilm description")
                .releaseDate(LocalDate.of(1995, 10, 10))
                .duration(125)
                .build();

        Film actualFilm = controller.update(updatedFilm);
        assertEquals(1, actualFilm.getId(), "ID не соответствует ожидаемому.");
        assertEquals("UpdatedFilm", actualFilm.getName(), "Имя не соответствует ожидаемому.");
        assertEquals("UpdatedFilm description", actualFilm.getDescription(),
                "Описание не соответствует ожидаемому.");
        assertEquals(LocalDate.of(1995, 10, 10), actualFilm.getReleaseDate(),
                "Дата релиза не соответствует ожидаемой.");
        assertEquals(125, actualFilm.getDuration());
        assertEquals(controller.findAll().get(0), actualFilm, "Сохраненный фильм не соответствует ожидаемому!");
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

    @Test
    public void shouldThrowValidationExceptionIfNoFilmToUpdate() {
        film = film.toBuilder().id(1).build();
        ValidationException e = assertThrows(ValidationException.class, () -> controller.update(film));
        assertEquals(e.getMessage(), "Фильм с id 1 отсутствует!");
    }
}
