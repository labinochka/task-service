package ru.effectivemobile.taskservice.dto.response;

import lombok.Setter;
import org.springframework.data.domain.Page;
import ru.effectivemobile.taskservice.dto.enumeration.Priority;
import ru.effectivemobile.taskservice.dto.enumeration.Status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Setter
public class TaskResponse {
    private UUID id;
    private String title;
    private String description;
    private Status status;
    private Priority priority;
    private UUID authorId;
    private UUID executorId;
    private Page<CommentResponse> comments;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
