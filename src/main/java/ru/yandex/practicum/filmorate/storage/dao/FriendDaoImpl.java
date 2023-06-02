package ru.yandex.practicum.filmorate.storage.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class FriendDaoImpl implements FriendDao {
    private final Logger log = LoggerFactory.getLogger(FilmDbStorage.class);
    private final JdbcTemplate jdbcTemplate;
    private final UserDbStorage userDbStorage;

    public FriendDaoImpl(JdbcTemplate jdbcTemplate, UserDbStorage userDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDbStorage = userDbStorage;
    }

    @Override
    public void addFriend(long userId, long friendId) {
        userDbStorage.get(userId);
        userDbStorage.get(friendId);

        String sql = "INSERT INTO friends (user_id, friend_id, status_id) " +
                "VALUES (?, ?, 1);";

        jdbcTemplate.update(sql, userId, friendId);
    }

    @Override
    public void deleteFriend(long userId, long friendId) {
        String sql = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?;";

        jdbcTemplate.update(sql, userId, friendId);
        jdbcTemplate.update(sql, friendId, userId);
    }

    @Override
    public List<User> getFriends(long userId) {
        String sql = "SELECT u.user_id, u.login, u.name, u.email, u.birthday " +
                "FROM friends AS f " +
                "JOIN users AS u ON f.friend_id = u.user_id " +
                "WHERE f.user_id = ?;";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            return User.builder()
                    .id(rs.getLong("user_id"))
                    .login(rs.getString("login"))
                    .name(rs.getString("name"))
                    .email(rs.getString("email"))
                    .birthday(rs.getDate("birthday").toLocalDate())
                    .build();
        }, userId);
    }

    @Override
    public List<User> getCommonFriends(long firstUserId, long secondUserId) {
        String sql = "SELECT u.user_id, u.login, u.name, u.email, u.birthday " +
                "FROM friends AS f1 " +
                "JOIN friends AS f2 ON f1.friend_id = f2.friend_id " +
                "JOIN users u ON f1.friend_id = u.user_id " +
                "WHERE f1.user_id = ? AND f2.user_id = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            return User.builder()
                    .id(rs.getLong("user_id"))
                    .login(rs.getString("login"))
                    .name(rs.getString("name"))
                    .email(rs.getString("email"))
                    .birthday(rs.getDate("birthday").toLocalDate())
                    .build();
        }, secondUserId, firstUserId);
    }
}
