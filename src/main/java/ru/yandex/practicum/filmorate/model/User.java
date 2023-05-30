package ru.yandex.practicum.filmorate.model;
import lombok.Data;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.validation.WithoutWhitespacesValidation;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Component
public class User {
    public static int usersCount = 0;
    public Set<Integer> friends = new HashSet<>();

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

}