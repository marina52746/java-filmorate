package ru.yandex.practicum.filmorate.dao.impl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.film.MpaStorage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Component("mpaDbStorage")
@Primary
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<MpaRating> findAll() {
        String sql = "select * from PUBLIC.MPA_RATING";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeMpa(rs));
    }

    private MpaRating makeMpa(ResultSet rs) throws SQLException {
        int mpaRatingId = rs.getInt("MPA_RATING_ID");
        String name = rs.getString("NAME");
        return new MpaRating(mpaRatingId, name);
    }

    @Override
    public MpaRating getById(int id) {
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("SELECT * FROM MPA_RATING WHERE MPA_RATING_ID = ?", id);
        if (genreRows.next()) {
            MpaRating mpa = new MpaRating(
                    genreRows.getInt("MPA_RATING_ID"),
                    genreRows.getString("NAME")
            );
            log.info("Найден MPA: {} {}", mpa.getId(), mpa.getName());
            return mpa;
        } else {
            log.info("MPA с идентификатором {} не найден.", id);
            throw new NotFoundException("MPA with id = " + id + " doesn't exist");
        }
    }
}

