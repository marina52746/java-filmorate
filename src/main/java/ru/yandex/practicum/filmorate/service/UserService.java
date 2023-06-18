package ru.yandex.practicum.filmorate.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> findAll() {
        return new ArrayList<>(userStorage.findAll());
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    public User getById(int id) {
        return userStorage.getById(id);
    }

    public void addFriend(int friend1Id, int friend2Id) {
        User friend1 = userStorage.getById(friend1Id);
        User friend2 = userStorage.getById(friend2Id);
        friend1.friends.add(friend2Id);
        friend2.friends.add(friend1Id);
    }

    public void deleteFriend(int friend1Id, int friend2Id) {
        User friend1 = userStorage.getById(friend1Id);
        User friend2 = userStorage.getById(friend2Id);
        friend1.friends.remove(friend2.getId());
        friend2.friends.remove(friend1.getId());
    }

    public Set<User> getCommonFriends(int friend1Id, int friend2Id) {
        User friend1 = userStorage.getById(friend1Id);
        User friend2 = userStorage.getById(friend2Id);
        return friend1.getFriends().stream()
                .filter(f -> friend2.getFriends().contains(f))
                .map(userStorage::getById)
                .collect(Collectors.toSet());
    }

    public Set<User> getFriends(int userId) {
        User user = userStorage.getById(userId);
        return user.getFriends().stream()
                .map(userStorage::getById)
                .collect(Collectors.toSet());
    }

}
