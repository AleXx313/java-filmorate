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

    private int id = 1;

    private UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> findAll() {
        return userStorage.getAll();
    }

    public User getUser(int id) {
        if (userStorage.get(id) == null) {
            throw new ModelNotFoundException("Пользователь с id " + id + " отсутствует!");
        }
        return userStorage.get(id);
    }

    public User create(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getId() == 0) {
            user.setId(id);
            id++;
            log.info("Пользователь с логином {} с id - {} добавлен!", user.getLogin(), user.getId());
        } else {
            log.info("Пользователь с логином {} с id - {} обновлен!", user.getLogin(), user.getId());
        }
        return userStorage.create(user);
    }

    public User update(User user) {
        if (userStorage.get(user.getId()) == null) {
            throw new ModelNotFoundException("Пользователь с id " + user.getId() + " отсутствует!");
        }
        log.info("Пользователь с логином {} с id - {} обновлен!", user.getLogin(), user.getId());
        return userStorage.update(user);
    }

    public void addFriends(int userId1, int userId2) {
        User friend1 = userStorage.get(userId1);
        User friend2 = userStorage.get(userId2);
        if (friend1 == null){
            throw new ModelNotFoundException("Пользователь с id " + userId1 + " отсутствует!");
        }
        if (friend2 == null){
            throw new ModelNotFoundException("Пользователь с id " + userId2 + " отсутствует!");
        }
        friend1.getFriends().add(userId2);
        friend2.getFriends().add(userId1);
        log.info("Пользователь с логином {} стал другом пользователя {}!", friend1.getLogin(), friend2.getLogin());

    }

    public void deleteFriends(int userId1, int userId2) {
        User friend1 = userStorage.get(userId1);
        User friend2 = userStorage.get(userId2);
        friend1.getFriends().remove(userId2);
        friend2.getFriends().remove(userId1);
        log.info("Пользователь с логином {} перестал быть другом пользователя {}!",
                friend1.getLogin(), friend2.getId());
    }

    public List<User> getFriends(int id) {
        List<User> friends = new ArrayList<>();
        User user = userStorage.get(id);
        for (Integer friendId : user.getFriends()) {
            friends.add(userStorage.get(friendId));
        }
        return friends;
    }

    public List<User> getCommonFriends(int userId1, int userId2) {
        List<User> commonFriends = new ArrayList<>(getFriends(userId1));
        commonFriends.retainAll(getFriends(userId2));
        return commonFriends;
    }
}
