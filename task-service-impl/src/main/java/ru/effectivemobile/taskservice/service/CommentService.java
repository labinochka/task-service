package ru.effectivemobile.taskservice.service;

import ru.effectivemobile.taskservice.dto.request.CommentRequest;
import ru.effectivemobile.taskservice.dto.response.CommentResponse;
import ru.effectivemobile.taskservice.security.userdetails.CustomUserDetails;

import java.util.UUID;

public interface CommentService {

    CommentResponse createComment(UUID taskId, CommentRequest request, CustomUserDetails user);

    CommentResponse updateComment(UUID id, CommentRequest request, CustomUserDetails user);

    void deleteComment(UUID id, CustomUserDetails user);
}
