package ru.skillfactory.banking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class ControllerAdviser extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(
            UserNotFoundException ex, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("value", "-1");
        body.put("message", "Пользователь не найден");

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InsufficientFundsToWriteOffException.class)
    public ResponseEntity<Object> handleInsufficientFundsToWriteOffException(
            InsufficientFundsToWriteOffException ex, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("value", "0");
        body.put("message", "Недостаточно средств на счёте");

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MoneyCouldNotBeNegativeException.class)
    public ResponseEntity<Object> handleMoneyCouldNotBeNegativeException(
            MoneyCouldNotBeNegativeException ex, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("value", "0");
        body.put("message", "Проверьте значение cash");

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
