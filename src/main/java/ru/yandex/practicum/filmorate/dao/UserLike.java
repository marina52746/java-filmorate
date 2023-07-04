package ru.yandex.practicum.filmorate.dao;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Component
@NoArgsConstructor
public class UserLike {
    private int userId;
    private int filmId;

    public UserLike(int userId, int filmId) {
        this.userId = userId;
        this.filmId = filmId;
    }
}
