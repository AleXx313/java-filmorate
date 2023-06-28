package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

public interface DirectorDao {

    List<Director> getDirectors();

    Director getDirectorById(long id);

    Director addDirector(Director director);

    Director updateDirector(Director director);

    void deleteDirector(long id);

    void addFilmDirector(long filmId, long directorId);

    List<Director> getDirectorsByFilmId(long filmId);

    void removeByFilm(long filmId);

}
