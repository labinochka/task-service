package ru.effectivemobile.taskservice.exception.model;

import java.util.UUID;

public class CommentNotFoundException extends NotFoundException {

    public CommentNotFoundException(UUID id) {
        super("Comment with id = %s - not found".formatted(id));
    }
}
