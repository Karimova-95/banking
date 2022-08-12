package ru.skillfactory.banking.services;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import ru.skillfactory.banking.dao.BankDAO;

@Data
@Service
@ConfigurationProperties(prefix = "money")
public class BankService {

    @Autowired
    private BankDAO dao;
    private String currency;

    public String getBalance(long userId) {
        return String.join(" ", String.valueOf(dao.getBalance(userId)), currency);
    }

    public int takeMoney(long userId, double takeCash) {
        return dao.takeMoney(userId, takeCash);
    }

    public int putMoney(long userId, double putCash) {
        return dao.putMoney(userId, putCash);
    }

}
