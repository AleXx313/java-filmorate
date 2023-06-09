package ru.yandex.practicum.filmorate.storage.interfaces;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Component
public interface UserStorage {

    User create(User user);

    User update(User user);

    void delete(long id);

    User get(long id);

    List<User> getAll();
}
