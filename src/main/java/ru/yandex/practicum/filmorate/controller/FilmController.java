package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
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
    public Film create(@Valid @RequestBody Film film) {
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
    public Film update(@Valid @RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Фильм с id " + film.getId() + " отсутствует!");
        }
        log.info("Фильм под названием {} с id - {} обновлен!", film.getName(), film.getId());
        films.put(film.getId(), film);
        return film;
    }
}
