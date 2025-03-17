package ru.effectivemobile.taskservice.exception.model;

import org.springframework.http.HttpStatus;

public class AccessForbiddenException extends ServiceException {

    public AccessForbiddenException() {
        super("Not enough rights", HttpStatus.FORBIDDEN);
    }
}
