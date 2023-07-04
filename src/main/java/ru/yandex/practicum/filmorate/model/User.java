package ru.yandex.practicum.filmorate.model;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.validation.WithoutWhitespacesValidation;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Data
@Component
@NoArgsConstructor
public class User {

    public Map<Integer, FriendStatus> friends = new HashMap<>();

    private int id;

    @NotEmpty(message = "User email can't be empty")
    @Email(message = "User email isn't correct")
    private String email;

    @NotEmpty(message = "User login can't be empty")
    @WithoutWhitespacesValidation
    private String login;

    private String name;

    @PastOrPresent(message = "User date of birthday can't be in future")
    private LocalDate birthday;

    public User(int userId, String email, String login, String name, LocalDate birth) {
        this.id = userId;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birth;
    }

    public User(String email, String login, String name, LocalDate birth) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birth;
    }
}