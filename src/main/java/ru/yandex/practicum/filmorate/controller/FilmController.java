package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
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
    public Film getFilm(@PathVariable(value = "id") @Positive @NotNull Long id) {
        return filmService.getFilm(id);
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

    @DeleteMapping("/{id}")
    public void deleteFilm(@PathVariable(value = "id") @Positive @NotNull Long id) {
        filmService.deleteFilm(id);
    }


    @PutMapping("/{id}/like/{userId}")//PUT /films/{id}/like/{userId}
    @ResponseBody
    public void setLike(@PathVariable(value = "id") @Positive @NotNull Long id,
                        @PathVariable(value = "userId") @Positive @NotNull Long userId) {
        filmService.setLikes(id, userId);
    }


    @DeleteMapping("/{id}/like/{userId}")//DELETE /films/{id}/like/{userId}
    public void deleteLike(@PathVariable(value = "id") @Positive @NotNull Long id,
                           @PathVariable(value = "userId") @Positive @NotNull Long userId) {
        filmService.removeLikes(id, userId);
    }

    @GetMapping("/popular")//GET /films/popular?count={count}
    @ResponseBody
    public List<Film> getPopularFilms(@RequestParam(value = "count",
            defaultValue = "10", required = false) @PathVariable(value = "count") @Positive @NotNull Integer count) {
        return filmService.getMostPopular(count);
    }
}
