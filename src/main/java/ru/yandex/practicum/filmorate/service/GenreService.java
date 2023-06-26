package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.interfaces.GenreDao;

import java.util.List;


@Service
public class GenreService {

    private final GenreDao genreDao;

    public GenreService(GenreDao genreDao) {
        this.genreDao = genreDao;
    }

    public List<Genre> getAll() {
        return genreDao.getAll();
    }

    public Genre getById(int id) {
        return genreDao.getById(id);
    }

    public void removeAllByFilm(long id) {
        genreDao.removeAllByFilm(id);
    }

    public List<Genre> getByFilm(long id) {
        return genreDao.getByFilm(id);
    }

    public void addFilmGenre(long filmId, int genreId) {
        genreDao.addFilmGenre(filmId, genreId);
    }


}
