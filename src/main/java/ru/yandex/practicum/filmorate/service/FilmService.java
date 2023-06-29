package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ModelNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmDao;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class FilmService {

    private final FilmDao filmDao;
    private final UserService userService;
    private final LikesService likesService;

    public FilmService(@Qualifier("filmDaoImpl") FilmDao filmDao,
                       UserService userService,
                       LikesService likesService) {
        this.filmDao = filmDao;
        this.userService = userService;
        this.likesService = likesService;
    }

    public List<Film> findAll() {
        return filmDao.getAll();
    }

    public Film getFilm(long id) {
        if (filmDao.get(id) == null) {
            throw new ModelNotFoundException("Фильм с id " + id + " отсутствует!");
        }
        return filmDao.get(id);
    }

    public Film create(Film film) {
        Film newFilm = filmDao.create(film);
        log.info("Фильм под названием {} с id - {} добавлен!", newFilm.getName(), newFilm.getId());
        return newFilm;
    }

    public Film update(Film film) {
        if (filmDao.get(film.getId()) == null) {
            throw new ModelNotFoundException("Фильм с id " + film.getId() + " отсутствует!");
        }
        log.info("Фильм под названием {} с id - {} обновлен!", film.getName(), film.getId());
        return filmDao.update(film);
    }

    public void deleteFilm(long id) {
        filmDao.delete(id);
    }

    public void setLikes(long filmId, long userId) {
        if (filmDao.get(filmId) == null) {
            throw new ModelNotFoundException("Фильм с id " + filmId + " отсутствует!");
        }
        if (userService.getUser(userId) == null) {
            throw new ModelNotFoundException("Пользователь с id " + userId + " отсутствует!");
        }
        likesService.addLikes(filmId, userId);
    }

    public void removeLikes(long filmId, long userId) {
        if (filmDao.get(filmId) == null) {
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

    public List<Film> getCommonFilms(long userId, long friendId){
        return filmDao.getCommonFilms(userId, friendId);
    }

    public List<Film> getSortedDirectorFilms (long directorId, String sortParam){

        switch (sortParam){
            case "likes":
                return filmDao.getByDirectorSortedByLikes(directorId);
            case "year":
                return filmDao.getByDirectorSortedByYear(directorId);
            default:
                throw new ModelNotFoundException("Неверный параметр запроса!");
        }
    }
}
