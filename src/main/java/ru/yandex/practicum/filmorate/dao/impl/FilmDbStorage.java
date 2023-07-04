package ru.yandex.practicum.filmorate.dao.impl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;
import static java.lang.Integer.parseInt;

@Slf4j
@Component("filmDbStorage")
@Primary
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final GenreDbStorage genreDbStorage;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, GenreDbStorage genreDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreDbStorage = genreDbStorage;
    }

    @Override
    public List<Film> findAll() {
        String sql = "select * from PUBLIC.FILM";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
    }

    public List<Integer> getGenresFromDb(int filmId) {
        List<Integer> arr = new ArrayList<>();
        String sql = "select GENRE_ID from PUBLIC.FILM_GENRE where FILM_ID = ?";
        jdbcTemplate.query(sql, (rs, rowNum) -> arr.add(rs.getInt("GENRE_ID")), filmId);
        return arr;
    }

    public void addGenresToDb(Film film) {
        if (film.getGenres() == null) return;
        film.setGenres(film.getGenres().stream().distinct().collect(Collectors.toList()));
        for (Genre genre : film.getGenres()) {
            String sql = "insert into FILM_GENRE (FILM_ID, GENRE_ID) values (?, ?)";
            jdbcTemplate.update(sql, film.getId(), genre.getId());
        }
        log.info("Added genres: " + film.getGenres() + " for film " + film.getId() + " to Database");
    }

    public void deleteGenresFromDb(String oldGenres, int filmId) {
        int[] genr = genresFromStr(oldGenres);
        for (int i = 0; i < genr.length; i++) {
            String sql = "delete from FILM_GENRE where FILM_ID = ? and GENRE_ID = ?";
            jdbcTemplate.update(sql, filmId, genr[i]);
        }
        log.info("Deleted genres: " + oldGenres + " from film " + filmId + " from Database");
    }

    public int[] genresFromStr(String str) {
        if (str.isBlank() || str.replace(" ","").equals("null") || str.replace(" ","").equals("[]"))
            return new int[]{};
        str = str.replace("[","").replace("]", "")
                .replaceAll("\\{", "").replaceAll("}", "")
                .replaceAll("\"","").replaceAll("id","")
                .replaceAll(" ","").replaceAll(":", "");
        if (str.isBlank() || str.isEmpty()) return new int[]{};
        return Arrays.stream(str.split(","))
                .mapToInt(Integer::parseInt)
                .toArray();
    }

    public Film makeFilm(ResultSet rs) throws SQLException {
        int id = rs.getInt("FILM_ID");
        String name = rs.getString("NAME");
        String description = rs.getString("DESCRIPTION");
        LocalDate releaseDate = rs.getDate("RELEASE_DATE").toLocalDate();
        int duration = rs.getInt("DURATION");
        MpaRating mpaRating = mpaFromString(rs.getString("MPA_RATING"));
        List<Genre> genres = new ArrayList<>();
        List<Integer> genresIds = new ArrayList<>(getGenresFromDb(id));
        genres = genresIds.stream().map(g -> genreDbStorage.getById(g)).collect(Collectors.toList());
        return new Film(id, name, description, releaseDate, duration, mpaRating, genres);
    }

    private String mpaToString(MpaRating mpa) {
        String str = "{ \"id\": " + mpa.getId() + ", \"name\": \"" + (mpa.getName() == null ?
                mpaGetName(mpa) : mpa.getName()) + "\"}";
        return str;
    }

    private String mpaGetName(MpaRating mpa) {
        SqlRowSet mpaRow = jdbcTemplate.queryForRowSet("SELECT NAME FROM MPA_RATING WHERE MPA_RATING_ID = ?", mpa.getId());
        if (mpaRow.next()) {
            String name = mpaRow.getString("NAME");
            return name;
        }
        return "";
    }

    private MpaRating mpaFromString(String mpaString) {
        MpaRating mpa = new MpaRating();
        String name;
        mpaString = mpaString.replace("{", "").replace("}", "")
                .replaceAll(":", "").replaceAll("\"", "")
                .replace("id","").replace("name", "").replaceAll(" ","");
        String[] parts = mpaString.split(",");
        int id = parseInt(parts[0]);
        mpa.setId(id);
        if (parts.length == 2) {
            name = parts[1];
            mpa.setName(name);
        }
        return mpa;
    }

    @Override
    public Film create(Film film) {
        SqlRowSet filmId = jdbcTemplate.queryForRowSet("SELECT MAX(FILM_ID) AS ID FROM FILM");
        if (filmId.next()) {
            film.setId(filmId.getInt("ID") + 1);
        }
        film.setMpaId(film.getMpa().getId());
        String sqlQuery = "insert into FILM(FILM_ID, NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_RATING, MPA_RATING_ID) " +
                "values (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sqlQuery,
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                mpaToString(film.getMpa()),
                film.getMpaId());
        addGenresToDb(film);
        log.info("Created film " + film.getName() + " with id = " + film.getId());
        return film;
    }

    @Override
    public Film update(Film film) {
        Film oldFilm = getById(film.getId());
        if (oldFilm.genresToString() != null && !(oldFilm.genresToString().equals(film.genresToString()))) {
            deleteGenresFromDb(oldFilm.genresToString(), film.getId());
        }
        film.setMpaId(film.getMpa().getId());
        String sqlQuery = "update FILM set " +
                "FILM_ID = ?, NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, " +
                "MPA_RATING = ?, MPA_RATING_ID = ? " +
                "where FILM_ID = ?";
        jdbcTemplate.update(sqlQuery,
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                mpaToString(film.getMpa()),
                film.getMpaId(),
                film.getId());
        addGenresToDb(film);
        log.info("Updated film " + film.getName() + " with id = " + film.getId());
        return film;
    }

    @Override
    public Film getById(int id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * FROM FILM WHERE FILM_ID = ?", id);
        if (filmRows.next()) {
            Film film = new Film(
                    filmRows.getInt("FILM_ID"),
                    filmRows.getString("NAME"),
                    filmRows.getString("DESCRIPTION"),
                    filmRows.getDate("RELEASE_DATE").toLocalDate(),
                    filmRows.getInt("DURATION"),
                    mpaFromString(filmRows.getString("MPA_RATING")),
                    null
            );
            film.setGenres(getGenresFromDb(film.getId()).stream()
                    .map(g -> genreDbStorage.getById(g))
                    .collect(Collectors.toList()));
            log.info("Найден фильм: {} {}", film.getId(), film.getName());
            return film;
        } else {
            log.info("Фильм с идентификатором {} не найден.", id);
            throw new NotFoundException("Film with id = " + id + " doesn't exist");
        }
    }

    @Override
    public List<Film> topNFilms(Integer n) {
        String sql = "select * from film left join " +
                "(select film_id as fi, count(*) as c from user_like group by film_id)\n" +
                "on film.film_id = fi\n" +
                "order by c desc" +
                " limit ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), n);
    }
}
