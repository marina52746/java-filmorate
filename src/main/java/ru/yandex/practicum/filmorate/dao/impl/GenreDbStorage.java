package ru.yandex.practicum.filmorate.dao.impl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.GenreStorage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Component("genreDbStorage")
@Primary
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> findAll() {
        String sql = "select * from PUBLIC.GENRE";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs));
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        int genreId = rs.getInt("GENRE_ID");
        String name = rs.getString("NAME");
        return new Genre(genreId, name);
    }

    @Override
    public Genre getById(int id) {
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("SELECT * FROM GENRE WHERE GENRE_ID = ?", id);
        if (genreRows.next()) {
            Genre genre = new Genre(
                    genreRows.getInt("GENRE_ID"),
                    genreRows.getString("NAME")
            );
            log.info("Найден жанр: {} {}", genre.getId(), genre.getName());
            return genre;
        } else {
            log.info("Жанр с идентификатором {} не найден.", id);
            throw new NotFoundException("Genre with id = " + id + " doesn't exist");
        }
    }
}
