package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ModelNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;

@Slf4j
@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserService userService;
    private final LikesService likesService;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage
            , UserService userService
            , LikesService likesService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
        this.likesService = likesService;
    }

    public List<Film> findAll() {
        return filmStorage.getAll();
    }

    public Film getFilm(long id) {
        if (filmStorage.get(id) == null) {
            throw new ModelNotFoundException("Фильм с id " + id + " отсутствует!");
        }
        return filmStorage.get(id);
    }

    public Film create(Film film) {
        Film newFilm = filmStorage.create(film);
        log.info("Фильм под названием {} с id - {} добавлен!", film.getName(), film.getId());
        return newFilm;
    }

    public Film update(Film film) {
        if (filmStorage.get(film.getId()) == null) {
            throw new ModelNotFoundException("Фильм с id " + film.getId() + " отсутствует!");
        }
        log.info("Фильм под названием {} с id - {} обновлен!", film.getName(), film.getId());
        return filmStorage.update(film);
    }
    public boolean deleteFilm(long id){
        return filmStorage.delete(id);
    }

    public void setLikes(long filmId, long userId) {
        if (filmStorage.get(filmId) == null) {
            throw new ModelNotFoundException("Фильм с id " + filmId + " отсутствует!");
        }
        if (userService.getUser(userId) == null) {
            throw new ModelNotFoundException("Пользователь с id " + userId + " отсутствует!");
        }
        likesService.addLikes(filmId, userId);
    }

    public void removeLikes(long filmId, long userId) {
        if (filmStorage.get(filmId) == null) {
            throw new ModelNotFoundException("Фильм с id " + filmId + " отсутствует!");
        }
        if (userService.getUser(userId) == null) {
            throw new ModelNotFoundException("Пользователь с id " + userId + " отсутствует!");
        }
        likesService.deleteLike(filmId, userId);
    }

    public List<Film> getMostPopular(int size) {
        return likesService.getPopularFilms(size);
    }
}
