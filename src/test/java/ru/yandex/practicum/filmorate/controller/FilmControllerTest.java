package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.ModelNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Rating;


import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class FilmControllerTest {

    private final FilmController controller;
    private Film film;
    private Film film2;
    private Film film3;

    @BeforeEach
    void init() {
        film = Film.builder()
                .name("Film")
                .description("Film description")
                .releaseDate(LocalDate.of(1990, 10, 10))
                .duration(120)
                .mpa(Rating.builder().id(1).build())
                .build();
        film2 = Film.builder()
                .name("Film2")
                .description("Film2 description")
                .releaseDate(LocalDate.of(1989, 10, 10))
                .duration(125)
                .mpa(Rating.builder().id(1).build())
                .build();
        film3 = Film.builder()
                .name("Film3")
                .description("Film3 description")
                .releaseDate(LocalDate.of(1988, 10, 10))
                .duration(115)
                .mpa(Rating.builder().id(1).build())
                .build();
    }

    @Test
    public void shouldReturnListWithFilms() {
        assertTrue(controller.findAll().isEmpty());
        controller.create(film);
        assertEquals(1, controller.findAll().size());
        controller.create(film2);
        assertEquals(2, controller.findAll().size());
        controller.create(film3);
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
                .mpa(Rating.builder().id(1).build())
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
}
