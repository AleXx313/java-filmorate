package ru.yandex.practicum.filmorate.storage.memory;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private long id = 1;
    private final Map<Long, Film> films;

    public InMemoryFilmStorage() {
        films = new HashMap<>();
    }

    private void setFilmId(Film film) {
        if (film.getId() == 0) {
            film.setId(id);
            id++;
        }
    }

    @Override
    public Film create(Film film) {
        setFilmId(film);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public boolean delete(long id) {
        Film film = films.remove(id);
        if (film == null) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public Film get(long id) {
        return films.get(id);
    }

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }
}