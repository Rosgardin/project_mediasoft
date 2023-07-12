package com.myexample.mydemo.repositories;

import java.sql.PreparedStatement;
import java.sql.Statement;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.myexample.mydemo.domain.User;
import com.myexample.mydemo.exceptions.MyAuthException;

@Repository
public class UserRepository implements IUserRepository {

    private static final String SQL_CREATE = "INSERT INTO USERS(USER_ID, USERNAME, EMAIL, PASSWORD) VALUES (NEXTVAL('USERS_SEQ'), ?, ?, ?)";
    private static final String SQL_COUNT_BY_EMAIL = "SELECT COUNT(*) FROM USERS WHERE EMAIL = ?";
    private static final String SQL_FIND_BY_ID = "SELECT USER_ID, USERNAME, EMAIL, PASSWORD " + "FROM USERS WHERE USER_ID = ?";
    private static final String SQL_FIND_BY_EMAIL = "SELECT USER_ID, USERNAME, EMAIL, PASSWORD " + "FROM USERS WHERE EMAIL = ?";


    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Integer create(String username, String email, String password) throws MyAuthException {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(10));
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, username);
                ps.setString(2, email);
                ps.setString(3, hashedPassword);

                return ps;
            }, keyHolder);

            return (Integer) keyHolder.getKeys().get("USER_ID");

        } catch (Exception e) {
            throw new MyAuthException("Invalid details. Failed to create account");
        }
    }

    @Override
    public User findByEmailAndPassword(String email, String password) throws MyAuthException {
        try{
            User user = jdbcTemplate.queryForObject(SQL_FIND_BY_EMAIL, userRowMapper, email);
            if(!BCrypt.checkpw(password, user.getPassword())) throw new MyAuthException("Invalid email/password");
            return user;
        }catch(EmptyResultDataAccessException e){
            throw new MyAuthException("Invalid email/password");
        }
    }

    @Override
    public Integer getCountByEmail(String email) {
        return jdbcTemplate.queryForObject(SQL_COUNT_BY_EMAIL, Integer.class, email);
    }

    @Override
    public User findById(Integer userId) {
        return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, userRowMapper, userId);
    }

    private RowMapper<User> userRowMapper = ((rs, rowNum) -> {
        return new User(
            rs.getInt("USER_ID"),
            rs.getString("USERNAME"),
            rs.getString("EMAIL"),
            rs.getString("PASSWORD"));
    });
}