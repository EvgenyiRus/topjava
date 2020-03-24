package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(@Valid User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
            saveRoles(user);
        } else if (namedParameterJdbcTemplate.update(
                "UPDATE users SET name=:name, email=:email, password=:password, " +
                        "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", parameterSource) == 0) {
            editRoles(user);
            return null;
        }
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        //jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", id);
        //deleteRoles(id); //todo по идее не нужно вызывать этот метод здесь, т.к. у нас каскадно удаляются роли ?
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Transactional
    public void deleteRoles(int id) {
        jdbcTemplate.update("Delete from user_roles where user_id:=?", id);
    }

    public void saveRoles(@Valid User user) {
        jdbcTemplate.batchUpdate(
                "Insert into user_roles (user_id, role) values (?,?)",
                user.getRoles(),
                user.getRoles().size(),
                (ParameterizedPreparedStatementSetter<Role>) (ps, role) -> {
                    ps.setInt(1, user.getId());
                    ps.setString(2, role.name());
                });
    }


    @Transactional
    public void editRoles(@Valid User user) {
        jdbcTemplate.batchUpdate("Update user_roles set role=? where user_id=?", user.getRoles(), user.getRoles().size(),
                (ps, role) -> {
                    ps.setString(1, role.name());
                    ps.setInt(2, user.getId());
                });
        //jdbcTemplate.update("Update user_roles set role=? where user_id=?", role, id);
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE id=?", ROW_MAPPER, id);
        User user = DataAccessUtils.singleResult(users);
        setRole(user);
        return user;
        //return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(@Valid String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        User user = DataAccessUtils.singleResult(users);
        return setRole(user);
    }

    @NotBlank
    public User setRole(@Valid User user) {
        if (user != null) {
            List<Role> roles = jdbcTemplate.queryForList("SELECT role FROM user_roles  WHERE user_id=?", Role.class, user.getId());
            user.setRoles(roles);
        }
        return user;
    }

    @Override
    public List<User> getAll() {
        List<User> users = jdbcTemplate.query("SELECT * FROM users ORDER BY name, email", ROW_MAPPER);
        users.stream().map(user -> setRole(user)).collect(Collectors.toList());
        return users;//jdbcTemplate.query("SELECT * FROM users ORDER BY name, email", ROW_MAPPER);
    }


    public List<User> getAll2() {
        return jdbcTemplate.query("select u.*,ur.role from users u left join user_roles ur on u.id = ur.user_id ORDER BY name, email", (rs, rowNum) -> {
                    //return jdbcTemplate.query("select * from users u ORDER BY name, email", (rs, rowNum) -> {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setName(rs.getString("name"));
                    user.setPassword(rs.getString("password"));
                    user.setEmail(rs.getString("email"));
                    user.setRegistered(rs.getTimestamp("registered"));
                    user.setEnabled(rs.getBoolean("enabled"));
                    user.setCaloriesPerDay(rs.getInt("calories_per_day"));
                    user.setRoles(jdbcTemplate.queryForList("SELECT role FROM user_roles  WHERE user_id=?", Role.class, user.getId()));
                    //user.setRoles((Collection<Role>) rs.getArray("role"));
                    return user;
                }
        );
    }
}
