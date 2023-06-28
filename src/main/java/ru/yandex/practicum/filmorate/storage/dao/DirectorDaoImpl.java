package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.interfaces.DirectorDao;

import javax.sql.RowSet;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DirectorDaoImpl implements DirectorDao {

    private final JdbcTemplate jdbcTemplate;

    public DirectorDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Director> getDirectors() {
        String sql = "SELECT * FROM directors;";
        return jdbcTemplate.query(sql, this::makeDirector);
    }



    @Override
    public Director getDirectorById(long id) {
        String sql = "SELECT * FROM directors WHERE director_id = ?;";
        return jdbcTemplate.queryForObject(sql, this::makeDirector, id);
    }

    @Override
    public Director addDirector(Director director) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("directors")
                .usingGeneratedKeyColumns("director_id");

        long id = simpleJdbcInsert.executeAndReturnKey(toMap(director)).longValue();
        return getDirectorById(id);
    }

    @Override
    public Director updateDirector(Director director) {
        getDirectorById(director.getId());
        String sql = "UPDATE directors SET name = ? WHERE director_id = ?;";
        jdbcTemplate.update(sql, director.getName(), director.getId());
        return getDirectorById(director.getId());
    }

    @Override
    public void deleteDirector(long id) {
        String sql = "DELETE FROM directors WHERE director_id = ?;";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void addFilmDirector(long filmId, long directorId) {
        String sql = "INSERT INTO films_directors (film_id, director_id) " +
                "VALUES (?, ?);";
        jdbcTemplate.update(sql, filmId, directorId);
    }

    @Override
    public List<Director> getDirectorsByFilmId(long filmId) {
        String sql = "SELECT * FROM directors AS d " +
                "INNER JOIN films_directors AS fd ON d.director_id = fd.director_id " +
                "INNER JOIN films AS f ON fd.film_id = f.film_id " +
                "WHERE f.film_id = ?;";

        return jdbcTemplate.query(sql, this::makeDirector, filmId);
    }

    @Override
    public void removeByFilm(long filmId) {
        String sql = "DELETE FROM films_directors WHERE film_id = ?;";
        jdbcTemplate.update(sql, filmId);
    }


    private Director makeDirector(ResultSet rs, int rowNum) throws SQLException {
        return Director.builder()
                .id(rs.getLong("director_id"))
                .name(rs.getString("name"))
                .build();
    }

    private Map<String, Object> toMap(Director director){
        Map<String, Object> values = new HashMap<>();
        values.put("name", director.getName());
        return values;
    }
}
