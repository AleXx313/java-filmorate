package ru.yandex.practicum.filmorate.storage.interfaces;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@Component
public interface FilmDao {

    Film create(Film film);

    Film update(Film film);

    void delete(long id);

    Film get(long id);

    List<Film> getAll();

    List<Film> getCommonFilms(long userId, long friendId);

    List<Film> getByDirectorSortedByLikes (long directorId);
    List<Film> getByDirectorSortedByYear (long directorId);
}
