package ru.yandex.practicum.filmorate.dao;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Component
@NoArgsConstructor
public class FilmGenre {
    private int filmId;
    private int genreId;

    public FilmGenre(int filmId, int genreId) {
        this.filmId = filmId;
        this.genreId = genreId;
    }
}
