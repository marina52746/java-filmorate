package ru.yandex.practicum.filmorate.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    @Autowired
    public FilmService (FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void addLike(int filmId, int userId) {
        Film film = filmStorage.getById(filmId);
        film.filmLikedUsersIds.add(userId);
    }

    public void deleteLike(int filmId, int userId) {
        Film film = filmStorage.getById(filmId);
        if (userId < 0)
            throw new NotFoundException("User can't have negative id");
        film.filmLikedUsersIds.remove(userId);
    }

    public Set<Film> topNFilms(Integer n) {
        if (filmStorage.findAll().size() != 0)
        return filmStorage.findAll().stream()
            .sorted((f1, f2) -> {
            int comp =  f2.filmLikedUsersIds.size() - f1.filmLikedUsersIds.size();
            return comp;
        }) .limit(n)
        .collect(Collectors.toSet());
        return Set.of();
    }

}
