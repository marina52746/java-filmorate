package ru.yandex.practicum.filmorate.dao.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.impl.UserDbStorage;
import ru.yandex.practicum.filmorate.model.User;
import java.util.List;

@Service
public class UserDbService {
    private final Logger log = LoggerFactory.getLogger(UserDbService.class);
    private final JdbcTemplate jdbcTemplate;
    private final UserDbStorage userDbStorage;

    @Autowired
    public UserDbService(JdbcTemplate jdbcTemplate, UserDbStorage userDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDbStorage = userDbStorage;
    }

    public void addFriend(int friend1Id, int friend2Id) {
        userDbStorage.getById(friend1Id);
        userDbStorage.getById(friend2Id);
        String sqlQuery = "insert into USER_FRIEND (USER_ID, USER_FRIEND_ID) " +
                "values (?, ?)";
        jdbcTemplate.update(sqlQuery,
                friend1Id,
                friend2Id
        );
    }

    public void deleteFriend(int friend1Id, int friend2Id) {
        userDbStorage.getById(friend1Id);
        userDbStorage.getById(friend2Id);
        String sqlQuery = "delete from USER_FRIEND " +
                "where USER_ID = ? and USER_FRIEND_ID = ?";
        jdbcTemplate.update(sqlQuery,
                friend1Id,
                friend2Id
        );
    }

    public List<User> getCommonFriends(int friend1Id, int friend2Id) {
        userDbStorage.getById(friend1Id);
        userDbStorage.getById(friend2Id);
        String sql = "SELECT distinct uf1.user_friend_id FROM PUBLIC.USER_FRIEND as uf1 join PUBLIC.USER_FRIEND as uf2 " +
                "on uf1.user_friend_id = uf2.user_friend_id " +
                "where uf1.user_id = ? and uf2.user_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> userDbStorage.makeUserById(rs), friend1Id, friend2Id);
    }

    public List<User> getFriends(int userId) {
        userDbStorage.getById(userId);
        String sql = "select USER_FRIEND_ID from PUBLIC.USER_FRIEND where USER_ID = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> userDbStorage.makeUserById(rs), userId);
    }
}
