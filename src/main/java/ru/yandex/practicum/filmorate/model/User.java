package ru.yandex.practicum.filmorate.model;
import lombok.Data;
import ru.yandex.practicum.filmorate.validation.WithoutWhitespacesValidation;
import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class User {
    @NotNull(message = "User id can't be null")
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

    public static int usersCount = 0;

}
