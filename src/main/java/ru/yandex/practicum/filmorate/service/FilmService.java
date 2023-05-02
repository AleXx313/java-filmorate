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

    private int id = 1;
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

    public Film getFilm(int id){
        if (filmStorage.get(id) == null) {
            throw new ModelNotFoundException("Фильм с id " + id + " отсутствует!");
        }
        return filmStorage.get(id);
    }

    public Film create(Film film) {
        if (film.getId() == 0) {
            film.setId(id);
            id++;
            log.info("Фильм под названием {} с id - {} добавлен!", film.getName(), film.getId());
        } else {
            log.info("Фильм под названием {} с id - {} обновлен!", film.getName(), film.getId());
        }
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        if (filmStorage.get(film.getId()) == null) {
            throw new ModelNotFoundException("Фильм с id " + film.getId() + " отсутствует!");
        }
        log.info("Фильм под названием {} с id - {} обновлен!", film.getName(), film.getId());
        return filmStorage.update(film);
    }

    public void setLikes(int filmId, int userId) {
        if (filmStorage.get(filmId) == null) {
            throw new ModelNotFoundException("Фильм с id " + filmId + " отсутствует!");
        }
        if (userStorage.get(userId) == null) {
            throw new ModelNotFoundException("Пользователь с id " + userId + " отсутствует!");
        }
        Film film = filmStorage.get(filmId);
        film.getLikes().add(userId);
    }

    public void removeLikes(int filmId, int userId) {
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
