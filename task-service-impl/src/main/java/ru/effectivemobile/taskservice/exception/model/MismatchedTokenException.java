package ru.effectivemobile.taskservice.exception.model;

import org.springframework.http.HttpStatus;

public class MismatchedTokenException extends ServiceException {

    public MismatchedTokenException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
