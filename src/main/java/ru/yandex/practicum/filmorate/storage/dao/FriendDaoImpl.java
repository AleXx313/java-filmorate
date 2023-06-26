package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.FriendDao;

import java.util.List;

@Repository
public class FriendDaoImpl implements FriendDao {

    private final JdbcTemplate jdbcTemplate;
    private final UserDaoImpl userDbStorage;

    public FriendDaoImpl(JdbcTemplate jdbcTemplate, UserDaoImpl userDaoImpl) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDbStorage = userDaoImpl;
    }

    @Override
    public void addFriend(long userId, long friendId) {
        userDbStorage.get(userId);
        userDbStorage.get(friendId);
        boolean hasRequest = false;
        if (checkFriendRequest(userId, friendId)) {
            hasRequest = true;
            String sql = "UPDATE friends SET status = TRUE WHERE friend_id = ? AND user_id = ?;";
            jdbcTemplate.update(sql, userId, friendId);
        }

        String sql = "INSERT INTO friends (user_id, friend_id, status) " +
                "VALUES (?, ?, ?);";

        jdbcTemplate.update(sql, userId, friendId, hasRequest);
    }

    @Override
    public void deleteFriend(long userId, long friendId) {
        String sql = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?;";

        jdbcTemplate.update(sql, userId, friendId);
        jdbcTemplate.update(sql, friendId, userId);
    }

    @Override
    public List<User> getFriends(long userId) {
        userDbStorage.get(userId);
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

    private boolean checkFriendRequest(long friendId, long userId) {
        String sql = "SELECT * FROM friends WHERE friend_id = ? AND user_id = ?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, friendId, userId);
        return rs.next();
    }
}
