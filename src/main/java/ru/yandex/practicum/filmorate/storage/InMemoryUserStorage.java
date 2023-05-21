package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {

    private long id = 1;
    private final Map<Long, User> users;

    public InMemoryUserStorage() {
        users = new HashMap<>();
    }

    private void setUserId(User user) {
        if (user.getId() == 0) {
            user.setId(id);
            id++;
        }
    }

    @Override
    public User create(User user) {
        setUserId(user);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User delete(long id) {
        return users.remove(id);
    }

    @Override
    public User get(long id) {
        return users.get(id);
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }
}
