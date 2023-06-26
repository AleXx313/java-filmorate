package ru.yandex.practicum.filmorate.storage.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.interfaces.GenreDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@Slf4j
public class GenreDaoImpl implements GenreDao {

    private final JdbcTemplate jdbcTemplate;

    public GenreDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> getAll() {
        String sql = "SELECT * FROM genres;";
        return jdbcTemplate.query(sql, this::makeGenre);
    }

    @Override
    public Genre getById(int id) {
        String sql = "SELECT * FROM genres WHERE genre_id = ?;";
        return jdbcTemplate.queryForObject(sql, this::makeGenre, id);
    }

    @Override
    public List<Genre> getByFilm(long id) {
        String sql =
                "SELECT g.genre_id, g.name " +
                        "FROM genres AS g " +
                        "JOIN films_genres AS fg ON g.genre_id = fg.genre_id " +
                        "JOIN films AS f ON fg.film_id = f.film_id " +
                        "WHERE f.film_id = ?;";

        return jdbcTemplate.query(sql, this::makeGenre, id);
    }

    @Override
    public void removeAllByFilm(long id) {
        String sql = "DELETE FROM films_genres WHERE film_id = ?;";

        jdbcTemplate.update(sql, id);
    }

    @Override
    public void addFilmGenre(long filmId, int genreId) {
        String sql = "INSERT INTO films_genres (film_id, genre_id) VALUES (?, ?);";

        jdbcTemplate.update(sql, filmId, genreId);
    }

    private Genre makeGenre(ResultSet rs, int rowNum) throws SQLException {
        return Genre.builder()
                .id(rs.getInt("genre_id"))
                .name(rs.getString("name"))
                .build();
    }
}
