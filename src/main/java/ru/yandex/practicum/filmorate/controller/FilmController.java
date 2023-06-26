package ru.yandex.practicum.filmorate.controller;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.dao.service.FilmDbService;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.FilmService;
import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final FilmService filmService;
    private final FilmDbService filmDbService;

    @Autowired
    public FilmController(FilmService filmService, FilmDbService filmDbService) {
        this.filmService = filmService;
        this.filmDbService = filmDbService;
    }

    @GetMapping
    public List<Film> findAll() {
        return filmService.findAll();
    }

    @GetMapping("/{id}")
    public Film findFilm(@PathVariable("id") Integer filmId) {
        return filmService.getById(filmId);
    }

    @PostMapping
    public Film create(@Valid @RequestBody String film) { //TODO ubrala valid dlia experimenta
        return filmDbService.createFilmFromString(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody String film) {
        return filmDbService.updateFilmFromString(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable("id") int filmId, @PathVariable("userId") int userId) {
        //filmService.addLike(filmId, userId);
        filmDbService.addLike(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") int filmId, @PathVariable("userId") int userId) {
        //filmService.deleteLike(filmId, userId);
        filmDbService.deleteLike(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> getTopFilms(@RequestParam(value = "count", required = false, defaultValue = "10") Integer count) {
        return filmService.topNFilms(count);
    }

}