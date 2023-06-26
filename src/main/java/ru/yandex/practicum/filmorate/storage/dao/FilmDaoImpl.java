package ru.yandex.practicum.filmorate.storage.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmDao;
import ru.yandex.practicum.filmorate.storage.interfaces.GenreDao;
import ru.yandex.practicum.filmorate.storage.interfaces.RatingDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class FilmDaoImpl implements FilmDao {

    private final JdbcTemplate jdbcTemplate;
    private final RatingDao ratingDao;
    private final GenreDao genreDao;

    public FilmDaoImpl(JdbcTemplate jdbcTemplate, RatingDao ratingDao, GenreDao genreDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.ratingDao = ratingDao;
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
        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());

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
    public void delete(long id) {
        String sql = "DELETE FROM films WHERE film_id = ?;";

        jdbcTemplate.update(sql, id);
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

    @Override
    public List<Film> getCommonFilms(long userId, long friendId) {
        String sql = "SELECT * " +
                "FROM films AS f " +
                "LEFT JOIN likes AS l ON f.film_id = l.film_id " +
                "LEFT JOIN likes AS l2 ON f.film_id = l2.film_id " +
                "WHERE l.user_id = ? AND l2.user_id = ? " +
                "GROUP BY f.film_id " +
                "ORDER BY COUNT(l.user_id) DESC;";
        return jdbcTemplate.query(sql, (this::makeFilm), userId, friendId);
    }

    private Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
        Film film = Film.builder()
                .id(rs.getLong("film_id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .mpa(ratingDao.getById(rs.getInt("rating_id")))
                //Добавить список жанров фильма
                .genres(genreDao.getByFilm(rs.getLong("film_id")))
                .build();
        return film;
    }

    private Map<String, Object> toMap(Film film) {
        Map<String, Object> values = new HashMap<>();
        values.put("name", film.getName());
        values.put("description", film.getDescription());
        values.put("release_date", film.getReleaseDate());
        values.put("duration", film.getDuration());
        values.put("rating_id", film.getMpa().getId());
        return values;
    }
}
