package ru.yandex.practicum.filmorate.storage.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User create(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");

        long id = simpleJdbcInsert.executeAndReturnKey(toMap(user)).longValue();
        return get(id);
    }

    @Override
    public User update(User user) {
        get(user.getId());

        String sql = "UPDATE users SET " +
                "login = ?, name = ?, email = ?, birthday = ? " +
                "WHERE user_id = ?;";
        jdbcTemplate.update(sql,
                user.getLogin(),
                user.getName(),
                user.getEmail(),
                user.getBirthday(),
                user.getId());

        return get(user.getId());
    }

    @Override
    public void delete(long id) {
        String sql = "DELETE FROM users WHERE user_id = ?;";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public User get(long id) {
        String sql = "SELECT * FROM users WHERE user_id =?;";
        User user = jdbcTemplate.queryForObject(sql, this::makeUser, id);
        return user;
    }

    @Override
    public List<User> getAll() {
        String sql = "SELECT * FROM users;";
        return jdbcTemplate.query(sql, this::makeUser);
    }

    private User makeUser(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .id(rs.getLong("user_id"))
                .login(rs.getString("login"))
                .name(rs.getString("name"))
                .email(rs.getString("email"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .build();
    }

    private Map<String, Object> toMap(User user) {
        Map<String, Object> values = new HashMap<>();
        values.put("login", user.getLogin());
        values.put("name", user.getName());
        values.put("email", user.getEmail());
        values.put("birthday", user.getBirthday());

        return values;
    }
}
