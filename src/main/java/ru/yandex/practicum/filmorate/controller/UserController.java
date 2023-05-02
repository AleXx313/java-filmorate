package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @ResponseBody
    public List<User> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public User getUser(@PathVariable(value = "id") Integer id) {
        if (id != null) {
            return userService.getUser(id);
        } else {
            throw new RuntimeException("Неверный параметр запроса!");
        }
    }

    @PostMapping
    @ResponseBody
    public User create(@Valid @RequestBody User user) {
        return userService.create(user);
    }

    @PutMapping
    @ResponseBody
    public User update(@Valid @RequestBody User user) {
        return userService.update(user);
    }

    @PutMapping("/{id}/friends/{friendId}") //PUT /users/{id}/friends/{friendId}
    public void addFriends(@PathVariable(value = "id") Integer id,
                           @PathVariable(value = "friendId") Integer friendId) {
        if (id != null && friendId != null) {
            userService.addFriends(id, friendId);
        } else {
            throw new RuntimeException("Неверный параметр запроса!");
        }
    }

    @DeleteMapping("/{id}/friends/{friendId}") //DELETE /users/{id}/friends/{friendId}
    public void deleteFriends(@PathVariable(value = "id") Integer id,
                              @PathVariable(value = "friendId") Integer friendId) {
        if (id != null && friendId != null) {
            userService.deleteFriends(id, friendId);
        } else {
            throw new RuntimeException("Неверный параметр запроса!");
        }
    }

    @GetMapping("/{id}/friends")//GET /users/{id}/friends
    @ResponseBody
    public List<User> getFriend(@PathVariable(value = "id") Integer id) {
        if (id != null) {
            return userService.getFriends(id);
        } else {
            throw new RuntimeException("Неверный параметр запроса!");
        }
    }

    @GetMapping("/{id}/friends/common/{otherId}")//GET /users/{id}/friends/common/{otherId}
    @ResponseBody
    public List<User> getCommonFriends(@PathVariable(value = "id") Integer id,
                                       @PathVariable(value = "otherId") Integer otherId) {
        if (id != null && otherId != null) {
            return userService.getCommonFriends(id, otherId);
        } else {
            throw new RuntimeException("Неверный параметр запроса!");
        }
    }
}
