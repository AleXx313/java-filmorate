package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private int id = 1;
    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Название фильма не может быть пустым!");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Слишком длинное описание фильма!");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза не может быть раньше чем дата прибытия поезда на вокзал Ла-Сиота!");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Длительность фильма должна быть больше 0 минут!");
        }
        if (film.getId() == 0) {
            film.setId(id);
            id++;
            log.info("Фильм под названием {} с id - {} добавлен!", film.getName(), film.getId());
        } else {
            log.info("Фильм под названием {} с id - {} обновлен!", film.getName(), film.getId());
        }
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Фильм с id " + film.getId() + " отсутствует!");
        }
        if (film.getName().isBlank() || film.getName() == null) {
            throw new ValidationException("Длительность фильма должна быть больше 0 минут!");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Слишком длинное описание фильма!");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException(
                    "Дата релиза не может быть раньше чем дата прибытия поезда на вокзал Ла-Сиота!");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Длительность фильма должна быть больше 0 минут!");
        }
        log.info("Фильм под названием {} с id - {} обновлен!", film.getName(), film.getId());
        films.put(film.getId(), film);
        return film;
    }
}
