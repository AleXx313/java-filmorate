package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    @ResponseBody
    public List<Film> findAll() {
        return filmService.findAll();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Film getFilm(@PathVariable(value = "id") Integer id) {
        if (id != null) {
            return filmService.getFilm(id);
        } else {
            throw new RuntimeException("Неверный параметр запроса!");
        }
    }

    @PostMapping
    @ResponseBody
    public Film create(@Valid @RequestBody Film film) {
        return filmService.create(film);
    }

    @PutMapping
    @ResponseBody
    public Film update(@Valid @RequestBody Film film) {
        return filmService.update(film);
    }


    @PutMapping("/{id}/like/{userId}")//PUT /films/{id}/like/{userId}
    public void setLike(@PathVariable(value = "id") Integer id,
                        @PathVariable(value = "userId") Integer userId) {
        if (id != null && userId != null) {
            filmService.setLikes(id, userId);
        } else {
            throw new RuntimeException("Неверный параметр запроса!");
        }
    }

    @DeleteMapping("/{id}/like/{userId}")//DELETE /films/{id}/like/{userId}
    public void deleteLike(@PathVariable(value = "id") Integer id,
                           @PathVariable(value = "userId") Integer userId) {
        if (id != null && userId != null) {
            filmService.removeLikes(id, userId);
        } else {
            throw new RuntimeException("Неверный параметр запроса!");
        }
    }

    @GetMapping("/popular")//GET /films/popular?count={count}
    @ResponseBody
    public List<Film> getPopularFilms(@RequestParam(value = "count",
            defaultValue = "10", required = false) @PathVariable(value = "count") Integer count) {
        return filmService.getMostPopular(count);
    }
}
