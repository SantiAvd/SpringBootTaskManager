package org.example.taskmanagerspringboot.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.example.taskmanagerspringboot.Service.TaskService;
import org.example.taskmanagerspringboot.Service.dto.requests.TaskCreateRequest;
import org.example.taskmanagerspringboot.Service.dto.response.TaskResponse;
import org.example.taskmanagerspringboot.Service.dto.requests.TaskUpdateRequest;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/tasks")
@Tag(
        name = "Tasks",
        description = "API для управления задачами"
)
public class TaskController {

   private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(
            summary = "Получить задачи",
            description = "Возвращает список задач с поддержкой фильтрации, пагинации и сортировки"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Задачи найдены"),
    })
    @GetMapping
    public ResponseEntity<Page<TaskResponse>> getAllTask(@Parameter(description = "Фильтр по названию задачи", example = "Spring") @RequestParam(required = false) String title, Pageable pageable) {
        return ResponseEntity.ok(taskService.getAllTask(title,pageable));
    }

    @Operation(
            summary = "Получить задачу по ID",
            description = "Возвращает задачу по указанному ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Задача найдена"),
            @ApiResponse(responseCode = "400", description = "Некорректный ID"),
            @ApiResponse(responseCode = "404", description = "Задача не найдена")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTask(@Parameter(description = "Идентификатор ID", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskBy(id));
    }

    @Operation(
            summary = "Создание задачи",
            description = "Создает новую задачу"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Задача создана"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации"),
    })

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskCreateRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(taskService.createTask(request));
    }

    @Operation(
            summary = "Обновление задачи",
            description = "Возможность изменить поля в существующей задаче"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Задача обновлена"),
            @ApiResponse(responseCode = "400", description = "ошибка валидации / некорректный ID"),
            @ApiResponse(responseCode = "404", description = "задача не найдена")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(@Parameter(description = "Идентификатор ID", example = "1") @PathVariable Long id, @Valid @RequestBody TaskUpdateRequest request) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(taskService.updateTaskBy(id,request));
    }
    @Operation(
            summary = "Удалить задачу по ID",
            description = "Удаляет задачу по указанному ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Задача удалена"),
            @ApiResponse(responseCode = "404", description = "Задача не найдена")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@Parameter(description = "Идентификатор ID", example = "1") @PathVariable Long id) {
        taskService.deleteTaskBy(id);
        return ResponseEntity.noContent().build();
    }
}
