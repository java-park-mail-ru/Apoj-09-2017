package application.db;

import application.models.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("MissortedModifiers")
@Transactional
public class UserDaoImpl implements UserDao {
    @Autowired
    private JdbcTemplate template;
    @NotNull
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoImpl.class);

    @Override
    @NotNull
    public Long addUser(@NotNull String login, @NotNull String password, @NotNull String email) {
        final String query = "INSERT INTO users(login, password, email) VALUES(?,?,?) RETURNING id";
        return template.queryForObject(query, Long.class, login, password, email);
    }

    @Override
    public void changeUserData(@NotNull User user) {
        try {
            final String query = "UPDATE users SET "
                    + "login = ?, "
                    + "email = ?, "
                    + "password = ? "
                    + "WHERE id = ?";
            template.update(query, user.getLogin(), user.getEmail(), user.getPassword(), user.getId());
        } catch (DuplicateKeyException e) {
            LOGGER.error("DuplicateKeyException in changeUserData");
        }
    }

    private static final RowMapper<User> USER_MAPPER = (res, num) ->
            new User(res.getLong("id"),
                    res.getString("login"),
                    res.getString("password"),
                    res.getString("email"),
                    res.getInt("sscore"),
                    res.getInt("mscore")
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
    public User getUser(@NotNull String login) {
        try {
            final String query = "SELECT * FROM users WHERE login = ?";
            return template.queryForObject(query, USER_MAPPER, login);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
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
    @NotNull
    public Integer updateSScore(long userId, boolean result) {
            String query = "UPDATE users SET sscore = score ";
            if (result) {
                query += "+ 25 ";
            } else {
                query += "- 25 ";
            }
            query += "WHERE id = ? RETURNING sscore";
            return template.queryForObject(query, Integer.class, userId);
    }

    @Override
    @NotNull
    public Integer updateMScore(long userId, boolean result) {
        String query = "UPDATE users SET mscore = score ";
        if (result) {
            query += "+ 25";
        } else {
            query += "- 25";
        }
        query += "WHERE id = ? RETURNING mscore";
        return template.queryForObject(query, Integer.class, userId);
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
