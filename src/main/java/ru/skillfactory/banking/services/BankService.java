package ru.skillfactory.banking.services;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.skillfactory.banking.config.CurrencyConfig;
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

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BankService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CurrencyConfig currencyConfig;
    @Autowired
    private OperationRepository operationRepository;
    private final OperationMapper operationMapper = Mappers.getMapper(OperationMapper.class);

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
        return String.join(" ", String.valueOf(user.getCash()), currencyConfig.getCurrency());
    }

    @Transactional(propagation = Propagation.REQUIRED)
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

    @Transactional(propagation = Propagation.REQUIRED)
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
        return operations.stream().map(operationMapper::toDto).collect(Collectors.toSet());
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public int transfer(long fromUserId, long toUserId, double cash) {
        takeMoney(fromUserId, cash, LocalDate.now());
        putMoney(toUserId, cash, LocalDate.now());
        return 1;
    }
}
