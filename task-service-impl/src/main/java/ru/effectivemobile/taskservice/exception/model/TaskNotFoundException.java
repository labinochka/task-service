package ru.effectivemobile.taskservice.exception.model;

import java.util.UUID;

public class TaskNotFoundException extends NotFoundException {

    public TaskNotFoundException(UUID id) {
        super("Task with id = %s - not found".formatted(id));
    }
}

