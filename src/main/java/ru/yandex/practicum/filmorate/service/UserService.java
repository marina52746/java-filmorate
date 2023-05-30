package ru.yandex.practicum.filmorate.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import java.util.Set;
import java.util.HashSet;

@Service
public class UserService {
    private final UserStorage userStorage;
    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
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
        if (friend1.friends.contains(friend2.getId()))
            friend1.friends.remove(friend2.getId());
        if (friend2.friends.contains(friend1.getId()))
            friend2.friends.remove(friend1.getId());
    }

    public Set<User> getCommonFriends(int friend1Id, int friend2Id) {
        User friend1 = userStorage.getById(friend1Id);
        User friend2 = userStorage.getById(friend2Id);
        Set<Integer> commonFriendsIds = new HashSet<Integer>(friend1.friends);
        commonFriendsIds.retainAll(friend2.friends);
        Set<User> commonFriends = new HashSet<>();
        for (int friendId : commonFriendsIds) {
            commonFriends.add(userStorage.getById(friendId));
        }
        return commonFriends;
    }

    public Set<User> getFriends(int userId) {
        User user = userStorage.getById(userId);
        Set<User> friends = new HashSet<>();
        for (int friendId : user.getFriends()) {
            friends.add(userStorage.getById(friendId));
        }
        return friends;
    }
}
