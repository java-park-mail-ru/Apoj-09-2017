package application.db;

import application.models.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class UserDaoImpl implements UserDao {
    @Autowired
    private JdbcTemplate template;

    @Override
    public Long addUser(String login, String password, String email) {
        try {
            final String query = "INSERT INTO users(login, password, email) VALUES(?,?,?) RETURNING id";
            return template.queryForObject(query, Long.class, login, password, email);
        } catch (DataAccessException e) {
            return null;
        }
    }

    @Override
    public void changeUserData(User user) {
        final String query = "UPDATE users SET "
                + "login = ?, "
                + "email = ?, "
                + "password = ? "
                + "WHERE id = ?";
        template.update(query, user.getLogin(), user.getEmail(), user.getPassword(), user.getId());
    }

    private static final RowMapper<User> USER_MAPPER = (res, num) -> new User(res.getLong("id"),
            res.getString("login"),
            res.getString("password"),
            res.getString("email")
    );

    @Override
    @Nullable
    public User getUser(long id) {
        try {
            final String query = "SELECT * FROM users WHERE id = ?";
            return template.queryForObject(query, USER_MAPPER, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    @Nullable
    public User getUser(String login) {
        try {
            final String query = "SELECT * FROM users WHERE login = ?";
            return template.queryForObject(query, USER_MAPPER, login);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    @Nullable
    public boolean checkSignup(@NotNull String login, @NotNull String email) {
        try {
            final String query = "SELECT COUNT(*) FROM users WHERE LOWER(login) = LOWER(?) OR LOWER(email) = LOWER(?)";
            final Long result = template.queryForObject(query, Long.class, login, email);
            return result == 0;
        } catch (EmptyResultDataAccessException e) {
            return true;
        }
    }

    @Override
    @Nullable
    public Long getIdByLogin(@NotNull String login) {
        try {
            final String query = "SELECT id FROM users WHERE LOWER(login) = LOWER(?)";
            return template.queryForObject(query, Long.class, login);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    @Nullable
    public Long getIdByEmail(@NotNull String email) {
        try {
            final String query = "SELECT id FROM users WHERE LOWER(email) = LOWER(?)";
            return template.queryForObject(query, Long.class, email);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void clear() {
        template.execute("TRUNCATE TABLE users CASCADE");
    }

}
