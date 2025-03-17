package ru.effectivemobile.taskservice.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.effectivemobile.taskservice.dto.enumeration.Priority;
import ru.effectivemobile.taskservice.dto.enumeration.Status;
import ru.effectivemobile.taskservice.dto.request.CommentRequest;
import ru.effectivemobile.taskservice.dto.request.TaskRequest;
import ru.effectivemobile.taskservice.dto.response.CommentResponse;
import ru.effectivemobile.taskservice.dto.response.TaskResponse;

import java.util.List;
import java.util.UUID;

@Tag(name = "Task API | Задачи")
@Schema(description = "Работа с задачами")
@ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "Ошибка валидации"),
        @ApiResponse(responseCode = "401", description = "Не пройдена авторизация"),
        @ApiResponse(responseCode = "403", description = "Недостаточно прав"),
        @ApiResponse(responseCode = "404", description = "Запрашиваемый ресурс не найден"),
        @ApiResponse(responseCode = "500", description = "Ведутся технические работы")
})
@RequestMapping("/api/v1/task-service/task")
public interface TaskApi {

    @Operation(summary = "Создание задачи")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задача создана")
    })
    @PostMapping
    TaskResponse create(@RequestBody @Valid TaskRequest request);

    @Operation(summary = "Получение задачи по id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задача получена")
    })
    @GetMapping("/{id}")
    TaskResponse getById(@PathVariable UUID id);

    @Operation(summary = "Получение задач c пагинацией")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задачи получены")
    })
    @GetMapping
    Page<TaskResponse> getAll(@RequestParam(value = "status", required = false) List<Status> status,
                              @RequestParam(value = "priority", required = false) List<Priority> priority,
                              @RequestParam(value = "authorId", required = false) List<UUID> authorId,
                              @RequestParam(value = "executorId", required = false) List<UUID> executorId,
                              @RequestParam(value = "search", required = false) String search,
                              @RequestParam(value = "isEarlyFirst", defaultValue = "false") boolean isEarlyFirst,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size);

    @Operation(summary = "Обновление задачи")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задача обновлена")
    })
    @PutMapping("/{id}")
    TaskResponse update(@PathVariable("id") UUID id, @RequestBody @Valid TaskRequest request);

    @Operation(summary = "Обновление статуса задачи")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Статус обновлен")
    })
    @PutMapping("/{taskId}/status")
    TaskResponse updateStatus(@PathVariable("taskId") UUID taskId, @RequestParam(value = "status") Status status);

    @Operation(summary = "Удаление задачи по id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задача удалена")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable("id") UUID id);

    @Operation(summary = "Создание комментария к задаче")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Комментарий создан")
    })
    @PostMapping("/comment/{taskId}")
    CommentResponse createComment(@PathVariable("taskId") UUID taskId, @RequestBody @Valid CommentRequest request);

    @Operation(summary = "Обновление комментария к задаче")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Комментарий обновлен")
    })
    @PutMapping("/comment/{id}")
    CommentResponse updateComment(@PathVariable("id") UUID id, @RequestBody @Valid CommentRequest request);

    @Operation(summary = "Удаление комментария по id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Комментарий удален")
    })
    @DeleteMapping("/comment/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteComment(@PathVariable("id") UUID id);
}
