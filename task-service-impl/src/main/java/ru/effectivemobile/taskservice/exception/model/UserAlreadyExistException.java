package ru.effectivemobile.taskservice.exception.model;

import org.springframework.http.HttpStatus;

public class UserAlreadyExistException extends ServiceException {

    public UserAlreadyExistException(String email) {
        super("User with email = %s already exist".formatted(email), HttpStatus.CONFLICT);
    }
}
