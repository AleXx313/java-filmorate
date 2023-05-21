package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ModelNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserService {

    private UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
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
        log.info("Пользователь с логином {} с id - {} добавлен!", user.getLogin(), user.getId());
        return newUser;
    }

    public User update(User user) {
        if (userStorage.get(user.getId()) == null) {
            throw new ModelNotFoundException("Пользователь с id " + user.getId() + " отсутствует!");
        }
        log.info("Пользователь с логином {} с id - {} обновлен!", user.getLogin(), user.getId());
        return userStorage.update(user);
    }

    public void addFriends(long userId1, long userId2) {
        User friend1 = userStorage.get(userId1);
        User friend2 = userStorage.get(userId2);
        if (friend1 == null || friend2 == null) {
            throw new ModelNotFoundException("Пользователи с id " + userId1 + " или с id " + userId2 + " отсутствует!");
        }
        friend1.getFriends().add(userId2);
        friend2.getFriends().add(userId1);
        log.info("Пользователь с логином {} стал другом пользователя {}!", friend1.getLogin(), friend2.getLogin());
    }

    public void deleteFriends(long userId1, long userId2) {
        User friend1 = userStorage.get(userId1);
        User friend2 = userStorage.get(userId2);
        if (friend1 == null || friend2 == null) {
            throw new ModelNotFoundException("Пользователи с id " + userId1 + " или с id " + userId2 + " отсутствует!");
        }
        friend1.getFriends().remove(userId2);
        friend2.getFriends().remove(userId1);
        log.info("Пользователь с логином {} перестал быть другом пользователя {}!",
                friend1.getLogin(), friend2.getId());
    }

    public List<User> getFriends(long id) {
        List<User> friends = new ArrayList<>();
        User user = userStorage.get(id);
        if (user == null) {
            throw new ModelNotFoundException("Пользователь с id " + id + " отсутствует!");
        }
        for (Long friendId : user.getFriends()) {
            friends.add(userStorage.get(friendId));
        }
        return friends;
    }

    public List<User> getCommonFriends(long userId1, long userId2) {
        List<User> commonFriends = new ArrayList<>(getFriends(userId1));
        commonFriends.retainAll(getFriends(userId2));
        return commonFriends;
    }
}
