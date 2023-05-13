package ru.yandex.practicum.filmorate.model;
import lombok.Data;
import ru.yandex.practicum.filmorate.validation.ReleaseDateValidation;
import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class Film {
    private int id;

    @NotEmpty(message = "Film name can't be empty")
    private String name;

    @Size(max = 200, message = "Film description length mustn't exceed 200 symbols")
    private String description;

    @ReleaseDateValidation
    private LocalDate releaseDate;

    @Positive(message = "Film duration must be positive")
    private int duration;

    public static int filmsCount = 0;
    
}
