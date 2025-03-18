package ru.effectivemobile.taskservice.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import ru.effectivemobile.taskservice.controller.TaskController;
import ru.effectivemobile.taskservice.dto.enumeration.Priority;
import ru.effectivemobile.taskservice.dto.enumeration.Status;
import ru.effectivemobile.taskservice.dto.request.CommentRequest;
import ru.effectivemobile.taskservice.dto.request.TaskRequest;
import ru.effectivemobile.taskservice.dto.response.CommentResponse;
import ru.effectivemobile.taskservice.dto.response.ShortTaskResponse;
import ru.effectivemobile.taskservice.dto.response.TaskResponse;
import ru.effectivemobile.taskservice.entity.CommentEntity;
import ru.effectivemobile.taskservice.entity.TaskEntity;
import ru.effectivemobile.taskservice.exception.model.CommentNotFoundException;
import ru.effectivemobile.taskservice.exception.model.TaskNotFoundException;
import ru.effectivemobile.taskservice.repository.CommentRepository;
import ru.effectivemobile.taskservice.repository.TaskRepository;
import ru.effectivemobile.taskservice.service.UserService;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TaskControllerTest extends AbstractTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    protected CommentRepository commentRepository;

    @Autowired
    private TaskController taskController;

    @Autowired
    private UserService userService;

    @AfterEach
    public void clear() {
        commentRepository.deleteAll();
        taskRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void getSelfBlocks() {
        taskRepository.save(generateHighTaskEntity());

        Page<ShortTaskResponse> tasks = taskController.getAll(null, null, null, null,
                null, false, 0, 10);

        assertAll(
                () -> assertNotNull(tasks),
                () -> assertEquals(1, tasks.getTotalElements())
        );
    }

    @Test
    public void getBlocksWithFilters() {
        taskRepository.save(generateHighTaskEntity());
        taskRepository.save(generateLowTaskEntity());

        Page<ShortTaskResponse> tasks = taskController.getAll(null, Collections.singletonList(Priority.LOW),
                null, null, null, false, 0, 10);

        assertAll(
                () -> assertNotNull(tasks),
                () -> assertEquals(1, tasks.getTotalElements())
        );
    }

    @Test
    public void getTask() {
        TaskEntity task = taskRepository.save(generateHighTaskEntity());

        TaskResponse taskResponse = taskController.getById(task.getId());

        assertAll(
                () -> assertNotNull(taskResponse),
                () -> assertEquals(task.getId(), taskResponse.id()),
                () -> assertEquals(task.getTitle(), taskResponse.title())
        );
    }

    @Test
    public void getTaskFail() {
        assertThrows(TaskNotFoundException.class, () -> taskController.getById(UUID.randomUUID()));
    }

    @Test
    public void createTaskSuccess() {
        TaskResponse taskResponse = taskController.create(new TaskRequest("test", "test",
                Status.FOR_EXECUTION, Priority.HIGH, userService.getCurrentUserId()));

        assertAll(
                () -> assertNotNull(taskResponse.id()),
                () -> assertEquals("test", taskResponse.title()),
                () -> assertEquals(Status.FOR_EXECUTION, taskResponse.status())
        );
    }

    @Test
    public void updateTaskSuccess() {
        TaskEntity task = taskRepository.save(generateHighTaskEntity());
        TaskResponse taskResponse = taskController.update(task.getId(), new TaskRequest("new test", "test",
                Status.FOR_EXECUTION, Priority.MEDIUM, userService.getCurrentUserId()));

        assertAll(
                () -> assertEquals(task.getId(), taskResponse.id()),
                () -> assertNotEquals(task.getTitle(), taskResponse.title()),
                () -> assertNotEquals(task.getPriority(), taskResponse.priority())
        );
    }

    @Test
    public void updateTaskFail() {
        assertThrows(TaskNotFoundException.class, () -> taskController.update(UUID.randomUUID(),
                new TaskRequest("new test", "test", Status.FOR_EXECUTION, Priority.MEDIUM,
                        userService.getCurrentUserId())));
    }

    @Test
    public void updateStatusSuccess() {
        TaskEntity task = taskRepository.save(generateHighTaskEntity());
        TaskResponse taskResponse = taskController.updateStatus(task.getId(), Status.IN_TESTING);

        assertAll(
                () -> assertEquals(task.getId(), taskResponse.id()),
                () -> assertNotEquals(task.getStatus(), taskResponse.status())
        );
    }

    @Test
    public void updateStatusFail() {
        assertThrows(TaskNotFoundException.class,
                () -> taskController.updateStatus(UUID.randomUUID(), Status.IN_TESTING));
    }

    @Test
    public void deleteTaskSuccess() {
        TaskEntity task = taskRepository.save(generateHighTaskEntity());
        taskController.delete(task.getId());
        Optional<TaskEntity> result = taskRepository.findById(task.getId());

        assertAll(
                () -> assertEquals(Optional.empty(), result)
        );
    }

    @Test
    public void deleteTaskFail() {
        assertThrows(TaskNotFoundException.class,
                () -> taskController.delete(UUID.randomUUID()));
    }

    @Test
    public void createCommentSuccess() {
        TaskEntity task = taskRepository.save(generateHighTaskEntity());
        CommentResponse commentResponse = taskController.createComment(task.getId(), new CommentRequest("test"));
        TaskResponse taskResponse = taskController.getById(task.getId());

        assertAll(
                () -> assertNotNull(commentResponse.id()),
                () -> assertEquals(1, taskResponse.comments().size()),
                () -> assertEquals("test", taskResponse.comments().get(0).comment())
        );
    }

    @Test
    public void createCommentFail() {
        assertThrows(TaskNotFoundException.class,
                () -> taskController.createComment(UUID.randomUUID(), new CommentRequest("test")));
    }

    @Test
    public void updateCommentSuccess() {
        TaskEntity task = taskRepository.save(generateHighTaskEntity());
        CommentEntity comment = commentRepository.save(generateCommentEntity(task));

        CommentResponse commentResponse = taskController.updateComment(comment.getId(), new CommentRequest("new test"));

        assertAll(
                () -> assertEquals(comment.getId(), commentResponse.id()),
                () -> assertNotEquals(comment.getComment(), commentResponse.comment())
        );
    }

    @Test
    public void updateCommentFail() {
        assertThrows(CommentNotFoundException.class,
                () -> taskController.updateComment(UUID.randomUUID(), new CommentRequest("test")));
    }

    @Test
    public void deleteCommentSuccess() {
        TaskEntity task = taskRepository.save(generateHighTaskEntity());
        CommentEntity comment = commentRepository.save(generateCommentEntity(task));

        taskController.deleteComment(comment.getId());
        Optional<CommentEntity> result = commentRepository.findById(comment.getId());

        assertAll(
                () -> assertEquals(Optional.empty(), result)
        );
    }

    @Test
    public void deleteCommentFail() {
        assertThrows(CommentNotFoundException.class,
                () -> taskController.deleteComment(UUID.randomUUID()));
    }
}
