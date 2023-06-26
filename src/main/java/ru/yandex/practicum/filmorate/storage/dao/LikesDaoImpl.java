package ru.yandex.practicum.filmorate.storage.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.interfaces.GenreDao;
import ru.yandex.practicum.filmorate.storage.interfaces.LikesDao;
import ru.yandex.practicum.filmorate.storage.interfaces.RatingDao;

import java.util.List;

@Repository
@Slf4j
public class LikesDaoImpl implements LikesDao {

    private final JdbcTemplate jdbcTemplate;
    private final RatingDao ratingDao;
    private final GenreDao genreDao;

    public LikesDaoImpl(JdbcTemplate jdbcTemplate, RatingDao ratingDao, GenreDao genreDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.ratingDao = ratingDao;
        this.genreDao = genreDao;
    }

    @Override
    public void addLikes(long filmId, long userId) {
        String sql = "INSERT INTO likes (film_id, user_id) VALUES (?, ?);";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public void deleteLike(long filmId, long userId) {
        String sql = "DELETE FROM likes WHERE film_id = ? AND user_id = ?;";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public List<Film> getPopularFilms(int size) {
        String sql = "SELECT f.film_id, f.name, f.description, f.release_date, f.duration, f.rating_id " +
                "FROM films AS f " +
                "LEFT OUTER JOIN likes AS l ON f.film_id = l.film_id " +
                "GROUP BY f.film_id " +
                "ORDER BY COUNT(l.user_id) DESC " +
                "LIMIT ?;";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Film film = Film.builder()
                    .id(rs.getLong("film_id"))
                    .name(rs.getString("name"))
                    .description(rs.getString("description"))
                    .releaseDate(rs.getDate("release_date").toLocalDate())
                    .duration(rs.getInt("duration"))
                    .mpa(ratingDao.getById(rs.getInt("rating_id")))
                    .genres(genreDao.getByFilm(rs.getLong("film_id")))
                    .build();
            return film;
        }, size);
    }
}
