package ru.yandex.practicum.filmorate.dao;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Component
@NoArgsConstructor
public class UserFriend {
    private int userId;
    private int userFriendId;

    public UserFriend(int userId, int userFriendId) {
        this.userId = userId;
        this.userFriendId = userFriendId;
    }
}
