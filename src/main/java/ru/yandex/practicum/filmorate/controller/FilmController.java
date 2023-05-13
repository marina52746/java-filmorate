package ru.yandex.practicum.filmorate.controller;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        Film.filmsCount++;
        film.setId(Film.filmsCount);
        films.put(film.getId(), film);
        log.info("Film created: " + film);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        if (!films.containsKey(film.getId()))
            throw new ValidationException("Can't update film with id = " + film.getId() + ", film doesn't exist");
        films.put(film.getId(), film);
        log.info("Film updated: " + film);
        return film;
    }
}
