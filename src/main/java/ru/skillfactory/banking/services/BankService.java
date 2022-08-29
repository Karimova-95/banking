package ru.skillfactory.banking.services;

import lombok.Data;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import ru.skillfactory.banking.dao.OperationRepository;
import ru.skillfactory.banking.dao.UserRepository;
import ru.skillfactory.banking.dto.OperationDto;
import ru.skillfactory.banking.exception.InsufficientFundsToWriteOffException;
import ru.skillfactory.banking.exception.MoneyCouldNotBeNegativeException;
import ru.skillfactory.banking.exception.UserNotFoundException;
import ru.skillfactory.banking.mapper.OperationMapper;
import ru.skillfactory.banking.model.Operation;
import ru.skillfactory.banking.model.OperationType;
import ru.skillfactory.banking.model.User;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Service
@ConfigurationProperties(prefix = "money")
public class BankService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OperationRepository operationRepository;
    private OperationMapper operationMapper = Mappers.getMapper(OperationMapper.class);
    private String currency;

    private User getUser(long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new UserNotFoundException(userId);
        }
    }

    private void updateUser(User user, double takeMoney) {
        user.setCash(user.getCash() + takeMoney);
        userRepository.save(user);
    }

    private void createOperation(User user, double takeMoney, OperationType operationType, LocalDate date) {
        Operation operation = new Operation();
        operation.setUser(user);
        operation.setCash(takeMoney);
        operation.setOperationType(operationType);
        operation.setDate(date);
        operationRepository.save(operation);
    }

    public String getBalance(long userId) {
        User user = getUser(userId);
        return String.join(" ", String.valueOf(user.getCash()), currency);
    }

    @Transactional
    public int takeMoney(long userId, double takeCash, LocalDate dateTime) {
        User user = getUser(userId);
        if (user.getCash() <= takeCash) {
            throw new InsufficientFundsToWriteOffException();
        } else if (takeCash <= 0) {
            throw new MoneyCouldNotBeNegativeException();
        }
        updateUser(user, -takeCash);
        createOperation(user, takeCash, OperationType.TAKE, dateTime);
        return 1;
    }

    @Transactional
    public int putMoney(long userId, double putCash, LocalDate dateTime) {
        User user = getUser(userId);
        if (putCash <= 0) {
            throw new MoneyCouldNotBeNegativeException();
        }
        updateUser(user, putCash);
        createOperation(user, putCash, OperationType.PUT, dateTime);
        return 1;
    }

    public Set<OperationDto> getOperationList(long userId, LocalDate from, LocalDate to) {
        Set<Operation> operations;
        if (from == null || to == null) {
            operations = operationRepository.findOperationByUserId(userId);
        } else {
            operations = operationRepository.findOperationByUserIdAndDateBetween(userId, from, to);
        }
        return operations.stream().map(operation -> operationMapper.toDto(operation)).collect(Collectors.toSet());
    }

    @Transactional(Transactional.TxType.NEVER)
    public int transfer(long fromUserId, long toUserId, double cash) {
        if (takeMoney(fromUserId, cash, LocalDate.now()) == 1) {
            putMoney(toUserId, cash, LocalDate.now());
        }
        return 1;
    }
}
