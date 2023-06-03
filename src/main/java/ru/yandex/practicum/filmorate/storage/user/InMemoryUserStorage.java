package ru.yandex.practicum.filmorate.storage.user;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private Map<Integer, User> users = new HashMap<>();

    public static int usersCount = 0;

    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    public User create(User user) {
        usersCount++;
        user.setId(usersCount);
        if (user.getName() == null || user.getName().isBlank())
            user.setName(user.getLogin());
        users.put(user.getId(), user);
        log.info("User created: " + user);
        return user;
    }

    public User update(User user) {
        if (!users.containsKey(user.getId()))
            throw new ValidationException("Can't update user with id = " + user.getId() + ", user doesn't exist");
        if (user.getName() == null || user.getName().isBlank())
            user.setName(user.getLogin());
        users.put(user.getId(), user);
        log.info("User updated: " + user);
        return user;
    }

    public User getById(int id) {
        if (users.containsKey(id))
            return users.get(id);
        throw new NotFoundException("User with id = " + id + " doesn't exist");
    }

}
