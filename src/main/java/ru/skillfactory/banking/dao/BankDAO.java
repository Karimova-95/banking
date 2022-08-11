package ru.skillfactory.banking.dao;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.skillfactory.banking.dto.User;
import ru.skillfactory.banking.dto.UserMapper;
import ru.skillfactory.banking.exception.InsufficientFundsToWriteOffException;
import ru.skillfactory.banking.exception.UserNotFoundException;

@Repository
public class BankDAO {

    private final NamedParameterJdbcTemplate jdbcTemp;
    private final UserMapper mapper;

    public BankDAO(NamedParameterJdbcTemplate namedJdbcTemplate) {
        this.jdbcTemp = namedJdbcTemplate;
        this.mapper = new UserMapper();
    }

    private User getUser(long id) {
        String sql = "SELECT * FROM account where id = :id";
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource("id", id);
        User user;
        try {
            user = jdbcTemp.queryForObject(sql, mapSqlParameterSource, mapper);
        } catch (Exception ex) {
            throw new UserNotFoundException(id);
        }
        return user;
    }

    private int update(User user, double cash) {
        String sqlUpdate = "UPDATE account SET cash = :cash WHERE id = :id";
        MapSqlParameterSource mapSqlParameterSource2 = new MapSqlParameterSource()
                .addValue("id", user.getId())
                .addValue("cash", user.getCash() + cash);
        return jdbcTemp.update(sqlUpdate, mapSqlParameterSource2);
    }

    public double getBalance(long userId) {
        User user = getUser(userId);
        return user.getCash();
    }

    public int takeMoney(long userId, double takeCash) {
        User user = getUser(userId);
        if (user.getCash() < takeCash) {
            throw new InsufficientFundsToWriteOffException();
        }
        return update(user, -takeCash);
    }

    public int putMoney(long userId, double putCash) {
        User user = getUser(userId);
        return update(user, putCash);
    }

}
