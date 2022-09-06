package ru.skillfactory.banking.exception;

public class MoneyCouldNotBeNegativeException extends RuntimeException {

    public MoneyCouldNotBeNegativeException() {
        super("Неверное значение cash");
    }
}
