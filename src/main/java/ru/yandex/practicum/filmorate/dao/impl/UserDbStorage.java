package ru.yandex.practicum.filmorate.dao.impl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component("userDbStorage")
@Primary
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> findAll() {
        String sql = "select * from PUBLIC.\"USER\"";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));
    }

    private User makeUser(ResultSet rs) throws SQLException {
        int id = rs.getInt("USER_ID");
        String email = rs.getString("EMAIL");
        String login = rs.getString("LOGIN");
        String name = rs.getString("NAME");
        LocalDate birth = rs.getDate("BIRTH").toLocalDate();
        return new User(id, email, login, name, birth);
    }

    public User makeUserById(ResultSet rs) throws SQLException {
        int id = rs.getInt("USER_FRIEND_ID");
        User user = getById(id);
        return user;
    }

    @Override
    public User create(User user) {
        if (user.getName() == null || user.getName().isBlank())
            user.setName(user.getLogin());
        SqlRowSet userId = jdbcTemplate.queryForRowSet("SELECT MAX(USER_ID) AS ID FROM \"USER\"");
        if (userId.next()) {
            user.setId(userId.getInt("ID") + 1);
        }
        String sqlQuery = "insert into \"USER\"(USER_ID, EMAIL, LOGIN, NAME, BIRTH) " +
                "values (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sqlQuery,
                user.getId(),
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday());
        log.info("Created user name = " + user.getName() + " with id = " + user.getId());
        return user;
    }

    @Override
    public User update(User user) {
        getById(user.getId());
        String sqlQuery = "update \"USER\" set " +
                "USER_ID = ?, EMAIL = ?, LOGIN = ?, NAME = ?, BIRTH = ? " +
                "where USER_ID = ?";
        jdbcTemplate.update(sqlQuery,
                user.getId(),
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        log.info("Updated user name = " + user.getName() + " with id = " + user.getId());
        return user;
    }

    @Override
    public User getById(int userId) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM \"USER\" WHERE USER_ID = ?", userId);
        if (userRows.next()) {
            User user = new User(
                    userRows.getInt("USER_ID"),
                    userRows.getString("EMAIL"),
                    userRows.getString("LOGIN"),
                    userRows.getString("NAME"),
                    userRows.getDate("BIRTH").toLocalDate()
                    );
            log.info("Найден пользователь: {} {}", user.getId(), user.getLogin());
            return user;
        } else {
            log.info("Пользователь с идентификатором {} не найден.", userId);
            throw new NotFoundException("User with id = " + userId + " doesn't exist");
        }
    }
}
