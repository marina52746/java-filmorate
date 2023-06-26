package ru.yandex.practicum.filmorate.dao.impl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
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

@Component("filmDbStorage")
@Primary
public class FilmDbStorage implements FilmStorage {
    private final Logger log = LoggerFactory.getLogger(FilmDbStorage.class);
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
        for (Genre genre : film.getGenres()) {
            String sql = "insert into FILM_GENRE (FILM_ID, GENRE_ID) values (?, ?)";
            jdbcTemplate.update(sql, film.getId(), genre.getId());
        }
    }

    public void deleteGenresFromDb(String oldGenres, int filmId) {
        int[] genr = genresFromStr(oldGenres);
        for (int i = 0; i < genr.length; i++) {
            String sql = "delete from FILM_GENRE where FILM_ID = ? and GENRE_ID = ?";
            jdbcTemplate.update(sql, filmId, genr[i]);
        }
    }

    public String genresToStr(int filmId) {
        List<Integer> genresIds = new ArrayList<>(getGenresFromDb(filmId));
        if (genresIds.size() == 0)
            return null;
        StringBuilder str = new StringBuilder("[");
        for (int id : genresIds) {
            str.append("{ \"id\": ");
            str.append(id);
            str.append("},");
        }
        String s = str.toString();
        s = s.substring(0, s.length() - 2) + "]";
        return s;
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

    public Film getFilmFromString(String filmStr) {
        int id = 0;
        String name = null;
        String description = null;
        LocalDate releaseDate = null;
        int duration = 0;
        MpaRating mpa = null;
        List<Genre> genres = null;
        String[] elems = filmStr.split("\n");
        for (String el : elems) {
            if (el.indexOf("\"id\"") != -1 && el.indexOf("{ \"id\"") == -1) {
                id = parseInt(el.replaceAll("\"", "")
                        .replace(":", "").replace("id", "")
                        .replaceAll(" ", "").replace(",",""));
            } else if (el.indexOf("name") != -1) {
                name = el.replaceAll("\"", "").replace("name", "")
                        .replace(":", "").replace(",","").trim();
            } else if (el.indexOf("releaseDate") != -1) {
                    String s = el.replaceAll("\"", "").replace(",","")
                            .replace("releaseDate", "").replace(":", "").trim();
                    String[] ymd = s.split("-");
                    releaseDate = LocalDate.of(parseInt(ymd[0]), parseInt(ymd[1]), parseInt(ymd[2]));
            } else if (el.indexOf("description") != -1) {
                description = el.replaceAll("\"", "").replace(",","")
                        .replace("description", "").replace(":", "").trim();
            } else if (el.indexOf("duration") != -1) {
                duration = parseInt(el.replaceAll("\"", "").replace(":", "")
                        .replace("duration", "").replaceAll(" ", "")
                        .replace(",",""));
            } else if (el.indexOf("mpa") != -1) {
                mpa = mpaFromString(el.replace("mpa", ""));
            } else if (el.indexOf("genres") != -1) {
                String genresStr = el.replace("\"genres\":", "").trim();
                List<Integer> genreIds = new ArrayList<>();
                for (int i = 0; i < genresFromStr(genresStr).length; i++) {
                    genreIds.add(genresFromStr(genresStr)[i]);
                }
                genres = genreIds.stream()
                        .map(g -> genreDbStorage.getById(g)).collect(Collectors.toList());
            }
        }
        if (name == null || name.isEmpty()) throw new ValidationException("Film name can't be empty");
        if (description.length() > 200)
            throw new ValidationException("Film description length mustn't exceed 200 symbols");
        if (releaseDate.isBefore(LocalDate.of(1895, 12, 28)))
            throw new ValidationException("Date must be greater or equal 1895-12-28");
        if (duration < 0) throw new ValidationException("Film duration must be positive");
        return new Film(id, name, description, releaseDate, duration, mpa, genres);
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
