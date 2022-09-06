package ru.skillfactory.banking.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.skillfactory.banking.dto.OperationDto;
import ru.skillfactory.banking.exception.InsufficientFundsToWriteOffException;
import ru.skillfactory.banking.exception.MoneyCouldNotBeNegativeException;
import ru.skillfactory.banking.exception.UserNotFoundException;
import ru.skillfactory.banking.model.OperationType;
import ru.skillfactory.banking.services.BankService;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@WebMvcTest(BankController.class)
class BankControllerTest {

    @Autowired
    protected MockMvc mvc;

    @MockBean
    private BankService bankService;

    @Test
    void getBalanceTest() throws Exception {
        String uri = "/balance/1";
        int expectedStatus = 200;
        when(bankService.getBalance(anyLong())).thenReturn("456.7 руб");
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .param("id", "1")).andReturn();
        int actualStatus = mvcResult.getResponse().getStatus();

        assertEquals(expectedStatus, actualStatus);
    }

    @Test
    void takeMoneyTest() throws Exception {
        String uri = "/take/1/7777.6";
        int expectedStatus = 200;
        when(bankService.takeMoney(anyLong(), anyDouble(), any())).thenReturn(1);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .param("id", "1")
                .param("cash", "7777.6")).andReturn();
        int actualStatus = mvcResult.getResponse().getStatus();

        assertEquals(expectedStatus, actualStatus);
    }

    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("getTestSourceProvider")
    void takeMoneyThrowExceptionTest(String description, String take, Class<RuntimeException> expected) throws Exception {
        String uri = "/take/1/200";
        int expectedStatus = 500;
        when(bankService.takeMoney(anyLong(), anyDouble(), any())).thenThrow(expected);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .param("id", "1")
                .param("cash", take)).andReturn();
        int actualStatus = mvcResult.getResponse().getStatus();

        assertEquals(expectedStatus, actualStatus);
    }

    private static Stream<Arguments> getTestSourceProvider() {
        return Stream.of(
                arguments("Take money more then exist", "200.00", InsufficientFundsToWriteOffException.class),
                arguments("Take negative money value", "-200.0", MoneyCouldNotBeNegativeException.class),
                arguments("User not found", "200.0", UserNotFoundException.class)
        );
    }

    @Test
    void putMoneyTest() throws Exception {
        String uri = "/put/1/7777.6";
        int expectedStatus = 200;
        when(bankService.putMoney(anyLong(), anyDouble(), any())).thenReturn(1);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .param("id", "1")
                .param("cash", "7777.6")).andReturn();
        int actualStatus = mvcResult.getResponse().getStatus();

        assertEquals(expectedStatus, actualStatus);
    }

    @Test
    void getOperationListTest() throws Exception {
        String uri = "/all?id=1&from=2022-05-15&to=2022-05-31";
        int expectedStatus = 200;
        OperationDto operation1 = new OperationDto(1L, 3000.00, OperationType.PUT, LocalDate.of(2022, 5, 16));
        OperationDto operation2 = new OperationDto(1L, 1750.00, OperationType.TAKE, LocalDate.of(2022, 5, 23));
        OperationDto operation3 = new OperationDto(1L, 700.00, OperationType.TAKE, LocalDate.of(2022, 5, 30));
        Set<OperationDto> operationSet = Set.of(operation1, operation2, operation3);
        when(bankService.getOperationList(anyLong(), any(), any())).thenReturn(operationSet);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .param("id", "1")
                .param("from", "2022-05-15")
                .param("to", "2022-05-31")).andReturn();
        int actualStatus = mvcResult.getResponse().getStatus();

        assertEquals(expectedStatus, actualStatus);
    }

    @Test
    void transferTest() throws Exception {
        String uri = "/transfer/1/2/450.00";
        int expectedStatus = 200;
        when(bankService.transfer(anyLong(), anyLong(), anyDouble())).thenReturn(1);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .param("from", "1")
                .param("to", "2")
                .param("cash", "450.00")).andReturn();
        int actualStatus = mvcResult.getResponse().getStatus();

        assertEquals(expectedStatus, actualStatus);
    }
}