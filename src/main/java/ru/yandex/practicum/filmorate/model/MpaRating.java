package ru.yandex.practicum.filmorate.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import javax.validation.constraints.NotEmpty;

@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
public class MpaRating {
    @NotEmpty(message = "MpaRating id can't be empty")
    private Integer id;
    private String name;

    public MpaRating(int id) {
        this.id = id;
    }
    /*
    G,
    PG,
    PG-13,
    R,
    NC-17
     */
}
