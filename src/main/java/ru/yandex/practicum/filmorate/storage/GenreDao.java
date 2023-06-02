package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;


public interface GenreDao {

    List<Genre> getAll();
    Genre getById(int id);
    List<Genre> getByFilm(long id);
    void removeAllByFilm(long id);
    void addFilmGenre (long filmId, int genreId);

}
