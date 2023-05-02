package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

@Component
public interface FilmStorage {

    Film create(Film film);

    Film update(Film film);

    Film delete(int id);

    Film get(int id);

    List<Film> getAll();
}
