package ru.yandex.practicum.filmorate.storage.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.RatingDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class RatingDaoImpl implements RatingDao {

    private final Logger log = LoggerFactory.getLogger(RatingDaoImpl.class);
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public RatingDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Rating getById(int id) {
        String sql = "SELECT * FROM rating WHERE rating_id = ?;";

        return jdbcTemplate.queryForObject(sql, this::makeRating, id);
    }

    @Override
    public List<Rating> getAll() {
        String sql = "SELECT * FROM rating;";

        return jdbcTemplate.query(sql, this::makeRating);
    }

    private Rating makeRating(ResultSet rs, int rowNum) throws SQLException {
        return Rating.builder()
                .id(rs.getInt("rating_id"))
                .name(rs.getString("name"))
                .build();
    }
}
