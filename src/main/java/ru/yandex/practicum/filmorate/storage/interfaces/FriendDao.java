package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendDao {

    void addFriend(long userId, long friendId);

    List<User> getFriends(long userId);

    void deleteFriend(long userId, long friendId);

    List<User> getCommonFriends(long firstUser, long secondUser);
}
