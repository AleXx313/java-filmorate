package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.interfaces.LikesDao;

import java.util.List;

@Service
public class LikesService {

    private final LikesDao likesDao;

    public LikesService(LikesDao likesDao) {
        this.likesDao = likesDao;
    }

    public void addLikes(long filmId, long userId) {
        likesDao.addLikes(filmId, userId);
    }

    public void deleteLike(long filmId, long userId) {
        likesDao.deleteLike(filmId, userId);
    }

    public List<Film> getPopularFilms(int size) {
        return likesDao.getPopularFilms(size);
    }
}
