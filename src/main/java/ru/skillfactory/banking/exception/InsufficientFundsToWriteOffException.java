package ru.skillfactory.banking.exception;

public class InsufficientFundsToWriteOffException extends RuntimeException {

    public InsufficientFundsToWriteOffException() {
        super("Недостаточно средств на счёте");
    }
}
