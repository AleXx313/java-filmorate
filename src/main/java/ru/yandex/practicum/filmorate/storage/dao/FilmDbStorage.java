package ru.yandex.practicum.filmorate.storage.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.RatingService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreDao;
import ru.yandex.practicum.filmorate.storage.RatingDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@Qualifier("filmDbStorage")
public class FilmDbStorage implements FilmStorage {
    private final Logger log = LoggerFactory.getLogger(FilmDbStorage.class);
    private final JdbcTemplate jdbcTemplate;
    private final RatingService ratingService;
    private final GenreDao genreDao;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, RatingService ratingService, GenreDao genreDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.ratingService = ratingService;
        this.genreDao = genreDao;
    }

    @Override
    public Film create(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("film_id");

        long id = simpleJdbcInsert.executeAndReturnKey(toMap(film)).longValue();
        //Добавить добавление в бд связи фильм-жанр
        if (!film.getGenres().isEmpty()) {
            for (Genre genre : film.getGenres()) {
                genreDao.addFilmGenre(id, genre.getId());
            }
        }
        return get(id);
    }

    @Override
    public Film update(Film film) {
        String sql = "UPDATE films SET " +
                "name = ?, description = ?, release_date = ?, duration = ?, rating_id = ? " +
                "WHERE film_id = ?;";
        jdbcTemplate.update(sql
                , film.getName()
                , film.getDescription()
                , film.getReleaseDate()
                , film.getDuration()
                , film.getMpa().getId()
                , film.getId());

        List<Genre> genres = genreDao.getByFilm(film.getId());
        if (!genres.isEmpty()) {
            genreDao.removeAllByFilm(film.getId());
        }
        genres = film.getGenres().stream().distinct().collect(Collectors.toList());
        if (!genres.isEmpty()) {
            for (Genre genre : genres) {
                genreDao.addFilmGenre(film.getId(), genre.getId());
            }
        }
        return get(film.getId());
    }


    @Override
    public boolean delete(long id) {
        String sql = "DELETE FROM films WHERE film_id = ?;";

        return jdbcTemplate.update(sql, id) > 0;
    }

    @Override
    public Film get(long id) {
        String sql = "SELECT * FROM films WHERE film_id = ?;";

        return jdbcTemplate.queryForObject(sql, this::makeFilm, id);
    }

    @Override
    public List<Film> getAll() {
        String sql = "SELECT * FROM films;";

        return jdbcTemplate.query(sql, (this::makeFilm));
    }

    private Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
        Film film = Film.builder()
                .id(rs.getLong("film_id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .mpa(ratingService.getById(rs.getInt("rating_id")))
                //Добавить список жанров фильма
                .genres(genreDao.getByFilm(rs.getLong("film_id")))
                .build();
        return film;
    }

    public Map<String, Object> toMap(Film film) {
        Map<String, Object> values = new HashMap<>();
        values.put("name", film.getName());
        values.put("description", film.getDescription());
        values.put("release_date", film.getReleaseDate());
        values.put("duration", film.getDuration());
        values.put("rating_id", film.getMpa().getId());
        return values;
    }
}
