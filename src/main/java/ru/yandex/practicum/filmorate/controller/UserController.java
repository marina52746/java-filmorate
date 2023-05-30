package ru.yandex.practicum.filmorate.controller;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserStorage userStorage;
    private final UserService userService;
    @Autowired
    public UserController() {
        this.userStorage = new InMemoryUserStorage();
        this.userService = new UserService(userStorage);
    }

    @GetMapping
    public List<User> findAll() {
        return userStorage.findAll();
    }

    @GetMapping("/{id}")
    public User findUser(@PathVariable("id") Integer userId){
        return userStorage.getById(userId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") Integer userId, @PathVariable("friendId") Integer userFriendId) {
        userService.addFriend(userId, userFriendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable("id") Integer userId, @PathVariable("friendId") Integer userFriendId) {
        userService.deleteFriend(userId, userFriendId);
    }

    @GetMapping("/{id}/friends")
    public Set<User> getFriends(@PathVariable("id") Integer userId) {
        return userService.getFriends(userId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Set<User> getCommonFriends(@PathVariable("id") Integer userId, @PathVariable("otherId") Integer otherId) {
        return userService.getCommonFriends(userId, otherId);
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return userStorage.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        return userStorage.update(user);
    }
}