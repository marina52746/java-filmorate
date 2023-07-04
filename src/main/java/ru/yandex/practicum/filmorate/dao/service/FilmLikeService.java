package ru.yandex.practicum.filmorate.dao.service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.impl.FilmDbStorage;
import ru.yandex.practicum.filmorate.dao.impl.UserDbStorage;

@Slf4j
@Service
public class FilmLikeService {
    private final JdbcTemplate jdbcTemplate;
    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userStorage;

    @Autowired
    public FilmLikeService(JdbcTemplate jdbcTemplate, FilmDbStorage filmDbStorage, UserDbStorage userStorage) {
        this.jdbcTemplate = jdbcTemplate;
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
        log.info("User " + userId + " add like to film " + filmId);
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
        log.info("User " + userId + " deleted like from film " + filmId);
    }
}
