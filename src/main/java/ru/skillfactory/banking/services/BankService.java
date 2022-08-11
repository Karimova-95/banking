package ru.skillfactory.banking.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.skillfactory.banking.dao.BankDAO;

import java.time.LocalDate;
import java.util.ArrayList;

@Service
public class BankService {

    @Autowired
    private BankDAO dao;

    public double getBalance(long userId) {
        return dao.getBalance(userId);
    }

    public int takeMoney(long userId, double takeCash) {
        return dao.takeMoney(userId, takeCash);
    }

    public int putMoney(long userId, double putCash) {
        return dao.putMoney(userId, putCash);
    }

    public ArrayList<Object> getOperationList(long userId, LocalDate from, LocalDate to) {
        return null;
    }

    public int transferMoney(long idCurrentUser, long idTargetUser, double cash) {
        try {
            return 1;
        } catch (Exception ex) {
            return 0;
        }
    }
}
