package ru.skillfactory.banking.exception;

public class MoneyCouldNotBeNegativeException extends RuntimeException {

    public MoneyCouldNotBeNegativeException() {
        super(String.format("Неверное значение cash"));
    }
}
