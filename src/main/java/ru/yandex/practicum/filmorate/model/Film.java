package ru.yandex.practicum.filmorate.model;
import lombok.Data;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.validation.ReleaseDateValidation;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Component
public class Film {
    public static int filmsCount = 0;

    public Set<Integer> filmLikedUsersIds = new HashSet<>();

    private int id;

    @NotEmpty(message = "Film name can't be empty")
    private String name;

    @Size(max = 200, message = "Film description length mustn't exceed 200 symbols")
    private String description;

    @ReleaseDateValidation
    private LocalDate releaseDate;

    @Positive(message = "Film duration must be positive")
    private int duration;
}
