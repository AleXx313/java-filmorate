package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@Component
public interface FilmStorage {

    Film create(Film film);

    Film update(Film film);

    boolean delete(long id);

    Film get(long id);

    List<Film> getAll();
}
