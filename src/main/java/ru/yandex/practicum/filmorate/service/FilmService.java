package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ModelNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
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

    public void setLikes(long filmId, long userId) {
        if (filmStorage.get(filmId) == null) {
            throw new ModelNotFoundException("Фильм с id " + filmId + " отсутствует!");
        }
        if (userStorage.get(userId) == null) {
            throw new ModelNotFoundException("Пользователь с id " + userId + " отсутствует!");
        }
        Film film = filmStorage.get(filmId);
        film.getLikes().add(userId);
    }

    public void removeLikes(long filmId, long userId) {
        if (filmStorage.get(filmId) == null) {
            throw new ModelNotFoundException("Фильм с id " + filmId + " отсутствует!");
        }
        if (userStorage.get(userId) == null) {
            throw new ModelNotFoundException("Пользователь с id " + userId + " отсутствует!");
        }
        Film film = filmStorage.get(filmId);
        film.getLikes().remove(userId);
    }

    public List<Film> getMostPopular(int size) {
        List<Film> allFilms = filmStorage.getAll();
        return allFilms.stream()
                .sorted((p1, p2) -> (p2.getLikes().size()) - p1.getLikes().size())
                .limit(size)
                .collect(Collectors.toList());
    }
}
