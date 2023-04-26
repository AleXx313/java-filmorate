package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private int id = 1;
    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
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
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        if (!users.containsKey(user.getId())) {
            throw new ValidationException("Пользователь с id " + user.getId() + " отсутствует!");
        }
        log.info("Пользователь с логином {} с id - {} обновлен!", user.getLogin(), user.getId());
        users.put(user.getId(), user);
        return user;
    }
}
