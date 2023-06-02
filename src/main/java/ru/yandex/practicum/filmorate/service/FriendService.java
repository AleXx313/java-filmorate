package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendDao;

import java.util.List;

@Service
public class FriendService {

    private final FriendDao friendDao;

    public FriendService(FriendDao friendDao) {
        this.friendDao = friendDao;
    }

    public void addFriends(long userId1, long userId2) {
        friendDao.addFriend(userId1, userId2);
    }

    public void deleteFriends(long userId1, long userId2) {
        friendDao.deleteFriend(userId1, userId2);
    }

    public List<User> getFriends(long id) {
        return friendDao.getFriends(id);
    }

    public List<User> getCommonFriends(long userId1, long userId2) {
        return friendDao.getCommonFriends(userId1, userId2);
    }

}
