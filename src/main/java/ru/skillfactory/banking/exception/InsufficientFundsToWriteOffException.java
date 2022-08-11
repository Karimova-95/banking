package ru.skillfactory.banking.exception;

public class InsufficientFundsToWriteOffException extends RuntimeException{

    public InsufficientFundsToWriteOffException() {
        super(String.format("Недостаточно средств на счёте"));
    }
}
