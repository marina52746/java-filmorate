package ru.yandex.practicum.filmorate.dao.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.impl.FilmDbStorage;
import ru.yandex.practicum.filmorate.dao.impl.UserDbStorage;
import ru.yandex.practicum.filmorate.model.Film;

@Service
public class FilmDbService {
    private final Logger log = LoggerFactory.getLogger(FilmDbService.class);
    private final JdbcTemplate jdbcTemplate;
    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userStorage;

    @Autowired
    public FilmDbService(JdbcTemplate jdbcTemplate, FilmDbStorage filmDbStorage, UserDbStorage userStorage) {
        this.jdbcTemplate=jdbcTemplate;
        this.filmDbStorage = filmDbStorage;
        this.userStorage = userStorage;
    }

    public void addLike(int filmId, int userId) {
        filmDbStorage.getById(filmId);
        userStorage.getById(userId);
        String sqlQuery = "insert into USER_LIKE (USER_ID, FILM_ID) " +
                "values (?, ?)";
        jdbcTemplate.update(sqlQuery,
                userId,
                filmId
                );
    }

    public void deleteLike(int filmId, int userId) {
        filmDbStorage.getById(filmId);
        userStorage.getById(userId);
        String sqlQuery = "delete from USER_LIKE " +
                "where USER_ID = ? and FILM_ID = ?";
        jdbcTemplate.update(sqlQuery,
                userId,
                filmId
        );
    }

    public Film createFilmFromString(String str) {
        return filmDbStorage.create(filmDbStorage.getFilmFromString(str));
    }

    public Film updateFilmFromString(String str) {
        return filmDbStorage.update(filmDbStorage.getFilmFromString(str));
    }

}
