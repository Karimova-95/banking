package ru.skillfactory.banking.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(long id) {
        super(String.format("Пользователь с Id %d не найден", id));
    }
}
