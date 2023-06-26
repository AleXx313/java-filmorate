package ru.yandex.practicum.filmorate.storage.memory;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmDao;

import java.util.*;

@Component
public class InMemoryFilmDao implements FilmDao {

    private long id = 1;
    private final Map<Long, Film> films;

    public InMemoryFilmDao() {
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
    public void delete(long id) {
        films.remove(id);
    }

    @Override
    public Film get(long id) {
        return films.get(id);
    }

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public List<Film> getCommonFilms(long userId, long friendId) {
        return null;
    }
}
