package ru.yandex.practicum.filmorate.controller;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        User.usersCount++;
        user.setId(User.usersCount);
        if (user.getName() == null)
            user.setName(user.getLogin());
        users.put(user.getId(), user);
        log.info("User created: " + user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        if (!users.containsKey(user.getId()))
            throw new ValidationException("Can't update user with id = " + user.getId() + ", user doesn't exist");
        if (user.getName() == null)
            user.setName(user.getLogin());
        users.put(user.getId(), user);
        log.info("User updated: " + user);
        return user;
    }
}
