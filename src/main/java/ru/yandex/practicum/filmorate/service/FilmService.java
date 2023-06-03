package ru.yandex.practicum.filmorate.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;

    @Autowired
    public FilmService (FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public Film getById(int id) {
        return filmStorage.getById(id);
    }

    public void addLike(int filmId, int userId) {
        Film film = filmStorage.getById(filmId);
        userService.getById(userId);
        film.filmLikedUsersIds.add(userId);
    }

    public void deleteLike(int filmId, int userId) {
        Film film = filmStorage.getById(filmId);
        userService.getById(userId);
        film.filmLikedUsersIds.remove(userId);
    }

    public Set<Film> topNFilms(Integer n) {
        if (filmStorage.findAll().size() != 0)
            return filmStorage.findAll()
                    .stream()
                    .sorted((f1, f2) -> f2.filmLikedUsersIds.size() - f1.filmLikedUsersIds.size())
                    .limit(n)
                    .collect(Collectors.toSet());
        return Set.of();
    }

}
