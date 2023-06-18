package ru.yandex.practicum.filmorate.storage.film;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();

    public static int filmsCount = 0;

    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    public Film create(Film film) {
        filmsCount++;
        film.setId(filmsCount);
        films.put(film.getId(), film);
        log.info("Film created: " + film);
        return film;
    }

    public Film update(Film film) {
        if (!films.containsKey(film.getId()))
            throw new ValidationException("Can't update film with id = " + film.getId() + ", film doesn't exist");
        films.put(film.getId(), film);
        log.info("Film updated: " + film);
        return film;
    }

    public Film getById(int id) {
        if (films.containsKey(id))
            return films.get(id);
        throw new NotFoundException("Film with id = " + id + " doesn't exist");
    }

}
