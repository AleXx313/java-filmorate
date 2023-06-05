package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ModelNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;
    private final FriendService friendService;

    public UserService(@Qualifier("userDbStorage") UserStorage userStorage, FriendService friendService) {
        this.userStorage = userStorage;
        this.friendService = friendService;
    }

    public List<User> findAll() {
        return userStorage.getAll();
    }

    public User getUser(long id) {
        if (userStorage.get(id) == null) {
            throw new ModelNotFoundException("Пользователь с id " + id + " отсутствует!");
        }
        return userStorage.get(id);
    }

    public User create(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        User newUser = userStorage.create(user);
        log.info("Пользователь с логином {} добавлен!", user.getLogin());
        return newUser;
    }

    public User update(User user) {
        if (userStorage.get(user.getId()) == null) {
            throw new ModelNotFoundException("Пользователь с id " + user.getId() + " отсутствует!");
        }
        log.info("Пользователь с логином {} с id - {} обновлен!", user.getLogin(), user.getId());
        return userStorage.update(user);
    }

    public void delete(long id) {
        userStorage.delete(id);
    }

    public void addFriends(long userId1, long userId2) {
        User friend1 = userStorage.get(userId1);
        User friend2 = userStorage.get(userId2);
        if (friend1 == null || friend2 == null) {
            throw new ModelNotFoundException("Пользователи с id " + userId1 + " или с id " + userId2 + " отсутствует!");
        }
        log.info("Пользователь с логином {} стал другом пользователя {}!", friend1.getLogin(), friend2.getLogin());
        friendService.addFriends(userId1, userId2);
    }

    public void deleteFriends(long userId1, long userId2) {
        User friend1 = userStorage.get(userId1);
        User friend2 = userStorage.get(userId2);
        if (friend1 == null || friend2 == null) {
            throw new ModelNotFoundException("Пользователи с id " + userId1 + " или с id " + userId2 + " отсутствует!");
        }
        log.info("Пользователь с логином {} перестал быть другом пользователя {}!",
                friend1.getLogin(), friend2.getId());
        friendService.deleteFriends(userId1, userId2);
    }

    public List<User> getFriends(long id) {

        return friendService.getFriends(id);
    }

    public List<User> getCommonFriends(long userId1, long userId2) {

        return friendService.getCommonFriends(userId1, userId2);
    }
}
