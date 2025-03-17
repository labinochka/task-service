package ru.effectivemobile.taskservice.exception.model;

import java.util.UUID;

public class UserNotFoundException extends NotFoundException {

    public UserNotFoundException() {
        super("User not found");
    }
}
