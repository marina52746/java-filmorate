package ru.yandex.practicum.filmorate.model;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.validation.ReleaseDateValidation;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Component
@NoArgsConstructor
public class Film implements Serializable {

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

    private int mpaId;

    private MpaRating mpa;

    public String genresToString() {
        List<Integer> genresIds = getGenres().stream().map(genre -> genre.getId()).collect(Collectors.toList());
        if (genresIds.size() == 0)
            return null;
        StringBuilder str = new StringBuilder("[");
        for (int id : genresIds) {
            str.append("{ \"id\": ");
            str.append(id);
            str.append("},");
        }
        String s = str.toString();
        s = s.substring(0, s.length() - 2) + "]";
        return s;
    }

    private List<Genre> genres;

    public Film(int id, String name, String description, LocalDate releaseDate, int duration, MpaRating mpa,
                List<Genre> genres) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        this.mpaId = mpa.getId();
        this.genres = genres;
    }

    public Film(String name, String description, LocalDate releaseDate, int duration, MpaRating mpa,
                List<Genre> genres) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        this.mpaId = mpa.getId();
        this.genres = genres;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\": ").append(getId()).append(",");
        sb.append("\"name\": \"").append(getName()).append("\",");
        sb.append("\"releaseDate\": \"").append(getReleaseDate()).append("\",");
        sb.append("\"description\": \"").append(getDescription()).append("\",");
        sb.append("\"duration\": ").append(getDuration()).append(",");
        sb.append(mpaToString(getMpa()));
        if (genres != null)
            sb.append("\"genres\": \"").append(genresToString()).append("\",");
        String s = sb.toString();
        s = s.substring(0, s.length() - 2);
        s = s + "}";
        return s;
    }

    private String mpaToString(MpaRating mpa) {
        String str = "\"mpa\": { \"id\": " + mpa.getId()
                + (mpa.getName() == null ? "" : ", \"name\": \"" + mpa.getName()) + "\"},";
        return str;
    }

}
