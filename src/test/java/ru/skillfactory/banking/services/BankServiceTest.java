package ru.skillfactory.banking.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.skillfactory.banking.config.CurrencyConfig;
import ru.skillfactory.banking.dao.OperationRepository;
import ru.skillfactory.banking.dao.UserRepository;
import ru.skillfactory.banking.dto.OperationDto;
import ru.skillfactory.banking.exception.InsufficientFundsToWriteOffException;
import ru.skillfactory.banking.exception.MoneyCouldNotBeNegativeException;
import ru.skillfactory.banking.exception.UserNotFoundException;
import ru.skillfactory.banking.model.Operation;
import ru.skillfactory.banking.model.OperationType;
import ru.skillfactory.banking.model.User;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringJUnitConfig(BankService.class)
@EnableConfigurationProperties(CurrencyConfig.class)
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
class BankServiceTest {

    @Autowired
    private BankService bankService;

    @Autowired
    private CurrencyConfig currencyConfig;

    @MockBean
    private OperationRepository operationRepository;

    @MockBean
    private UserRepository userRepository;

    @Test
    void getBalanceWithExistUserTest() {
        User user = new User(4, 13545.75, Collections.emptySet());
        String expected = "13545.75 руб.";
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        currencyConfig.setCurrency("руб.");
        String actual = bankService.getBalance(1);

        assertEquals(expected, actual);
    }

    @Test
    void getBalanceWithNoExistUserTest() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> bankService.getBalance(1));
    }

    @Test
    void takeMoneyTest() {
        int expected = 1;
        User user = new User(1, 13545.75, Collections.emptySet());
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        int actual = bankService.takeMoney(1, 200.00, LocalDate.of(2022, 9, 22));

        assertEquals(expected, actual);
    }

    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("getTestSourceProvider")
    void takeMoneyTest(String description, double take, Class<RuntimeException> expected) {
        User user = new User();
        user.setId(1);
        user.setCash(100.50);
        user.setOperations(Collections.emptySet());
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        assertThrows(expected, () -> bankService.takeMoney(1, take, LocalDate.of(2022, 9, 22)));
    }

    private static Stream<Arguments> getTestSourceProvider() {
        return Stream.of(
                arguments("Take money more then exist", 200.00, InsufficientFundsToWriteOffException.class),
                arguments("Take negative money value", -200.0, MoneyCouldNotBeNegativeException.class)
        );
    }

    @Test
    void putMoneyTest() {
        int expected = 1;
        User user = new User(1, 100.50, Collections.emptySet());
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        int actual = bankService.putMoney(1, 200.00, LocalDate.of(2022, 9, 22));

        assertEquals(expected, actual);
    }

    @Test
    void putNegativeMoneyTest() {
        User user = new User(1, 100.50, Collections.emptySet());
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        assertThrows(MoneyCouldNotBeNegativeException.class, () -> bankService.putMoney(1, -200, LocalDate.of(2022, 9, 22)));
    }

    @Test
    void getOperationListWithDatesTest() {
        Operation operation1 = new Operation();
        operation1.setId(1L);
        operation1.setDate(LocalDate.of(2020, 1, 3));
        operation1.setOperationType(OperationType.TAKE);
        operation1.setCash(300.00);
        operation1.setUser(new User(1L, 650.00, Collections.emptySet()));

        Operation operation2 = new Operation();
        operation2.setId(2L);
        operation2.setDate(LocalDate.of(2020, 1, 28));
        operation2.setOperationType(OperationType.TAKE);
        operation2.setCash(1000.00);
        operation2.setUser(new User(1L, 650.00, Collections.emptySet()));

        Operation operation3 = new Operation();
        operation3.setId(3L);
        operation3.setDate(LocalDate.of(2020, 1, 15));
        operation3.setOperationType(OperationType.PUT);
        operation3.setCash(4000.00);
        operation3.setUser(new User(1L, 150.00, Collections.emptySet()));
        Set<Operation> expected = Set.of(operation1, operation2, operation3);
        when(operationRepository.findOperationByUserIdAndDateBetween(anyLong(), any(), any())).thenReturn(expected);

        Set<OperationDto> actual = bankService.getOperationList(1L, LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 31));

        assertEquals(expected.size(), actual.size());
    }

    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("getTestDateSourceProvider")
    void getOperationListWithoutDatesTest(String description, LocalDate from, LocalDate to) {
        Operation operation1 = new Operation();
        operation1.setId(1L);
        operation1.setDate(LocalDate.of(2020, 1, 3));
        operation1.setOperationType(OperationType.TAKE);
        operation1.setCash(300.00);
        operation1.setUser(new User(1L, 650.00, Collections.emptySet()));
        Operation operation2 = new Operation();

        operation2.setId(2L);
        operation2.setDate(LocalDate.of(2020, 1, 28));
        operation2.setOperationType(OperationType.TAKE);
        operation2.setCash(1000.00);
        operation2.setUser(new User(1L, 650.00, Collections.emptySet()));
        Operation operation3 = new Operation();

        operation3.setId(3L);
        operation3.setDate(LocalDate.of(2020, 1, 15));
        operation3.setOperationType(OperationType.PUT);
        operation3.setCash(4000.00);
        operation3.setUser(new User(1L, 150.00, Collections.emptySet()));
        Set<Operation> expected = Set.of(operation1, operation2, operation3);
        when(operationRepository.findOperationByUserId(anyLong())).thenReturn(expected);

        Set<OperationDto> actual = bankService.getOperationList(1L, from, to);

        assertEquals(expected.size(), actual.size());
    }


    private static Stream<Arguments> getTestDateSourceProvider() {
        return Stream.of(
                arguments("Local date To is NULL", LocalDate.of(2020, 1, 1), null),
                arguments("Local date From is NULL", null, LocalDate.of(2020, 1, 31)),
                arguments("All dates is NULL", null, null)
        );
    }

    @Test
    void transferWhenNotEnoughMoneyTest() {
        User userFrom = new User(1L, 100.00, Collections.emptySet());
        when(userRepository.findById(1L)).thenReturn(Optional.of(userFrom));
        User userTo = new User(2L, 850.00, Collections.emptySet());
        when(userRepository.findById(2L)).thenReturn(Optional.of(userTo));

        assertThrows(InsufficientFundsToWriteOffException.class, () -> bankService.transfer(1L, 2L, 350.00));
    }


    @Test
    void transferNegativeMoneyTest() {
        User userFrom = new User(1L, 100.00, Collections.emptySet());
        when(userRepository.findById(1L)).thenReturn(Optional.of(userFrom));
        User userTo = new User(2L, 850.00, Collections.emptySet());
        when(userRepository.findById(2L)).thenReturn(Optional.of(userTo));

        assertThrows(MoneyCouldNotBeNegativeException.class, () -> bankService.transfer(1L, 2L, -350.00));
    }

    @Test
    void transferSuccessTest() {
        int expected = 1;
        User userFrom = new User(1, 3500.00, Collections.emptySet());
        User userTo = new User(2, 100.50, Collections.emptySet());

        when(userRepository.findById(1L)).thenReturn(Optional.of(userFrom));
        when(userRepository.findById(2L)).thenReturn(Optional.of(userTo));

        int actual = bankService.transfer(1L, 2L, 500);

        assertEquals(expected, actual);
    }
}