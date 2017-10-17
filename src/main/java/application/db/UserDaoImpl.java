package application.db;

import application.models.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.sql.PreparedStatement;

public class UserDaoImpl implements UserDao {
    @Autowired
    private JdbcTemplate template;

    @Override
    public long addUser(String login, String password, String email) {
        final GeneratedKeyHolder idHolder = new GeneratedKeyHolder();
        template.update(connection -> {
            final String query = "INSERT INTO Users(login, password, email) VALUES(?,?,?) returning id";
            final PreparedStatement pst = connection.prepareStatement(
                    query,
                    PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setString(1, login);
            pst.setString(2, password);
            pst.setString(3, email);
            return pst;
        }, idHolder);
        return idHolder.getKey().longValue();
    }

    @Override
    public void changeUserData(User user) {
        final String query = "UPDATE Users SET "
                + "login = COALESCE (?, login), "
                + "email = COALESCE (?, email), "
                + "password = COALESCE (?, password) "
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
    public Long getId(@Nullable String login, @Nullable String email) {
        try {
            final String query = "SELECT id FROM users WHERE LOWER(login) = LOWER(?) OR LOWER(email) = LOWER(?)";
            return template.queryForObject(query, Long.class, login, email);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

}
