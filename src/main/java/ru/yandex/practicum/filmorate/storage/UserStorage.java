package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Component
public interface UserStorage {

    User create(User user);

    User update(User user);

    User delete(long id);

    User get(long id);

    List<User> getAll();
}
