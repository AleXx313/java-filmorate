package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.NotNull;
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
    public User getUser(@PathVariable(value = "id") @Positive @NotNull Integer id) {
        return userService.getUser(id);
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
    public void addFriends(@PathVariable(value = "id") @Positive @NotNull Integer id,
                           @PathVariable(value = "friendId") @Positive @NotNull Integer friendId) {
        userService.addFriends(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}") //DELETE /users/{id}/friends/{friendId}
    public void deleteFriends(@PathVariable(value = "id") @Positive @NotNull Integer id,
                              @PathVariable(value = "friendId") @Positive @NotNull Integer friendId) {
        userService.deleteFriends(id, friendId);
    }

    @GetMapping("/{id}/friends")//GET /users/{id}/friends
    @ResponseBody
    public List<User> getFriend(@PathVariable(value = "id") @Positive @NotNull Integer id) {
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")//GET /users/{id}/friends/common/{otherId}
    @ResponseBody
    public List<User> getCommonFriends(@PathVariable(value = "id") @Positive @NotNull Integer id,
                                       @PathVariable(value = "otherId") @Positive @NotNull Integer otherId) {
        return userService.getCommonFriends(id, otherId);
    }
}
