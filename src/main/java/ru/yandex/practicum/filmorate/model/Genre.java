package ru.yandex.practicum.filmorate.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import javax.validation.constraints.NotEmpty;

@Data
@Component
@NoArgsConstructor
@AllArgsConstructor
public class Genre {
    @NotEmpty(message = "FilmGenre id can't be empty")
    private int id;
    private String name; // Комедия, Драма, Мультфильм, Триллер, Документальный, Боевик

}
