package org.example.taskmanagerspringboot.Service.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.example.taskmanagerspringboot.model.TaskStatus;

public class TaskCreateRequest {

    @Schema(
            description = "Название задачи",
            example = "Купить хлеб"
    )
    @NotBlank(message = "Название задачи не может быть пустым")
    @Size(min = 3, max = 100)
    private String title;

    @Schema(
            description = "Описания задачи",
            example = "Купить бездрожжевой хлеб с отрубями и шоколадной начинкой"
    )
    @Size(max = 200, message = "Максимальное кол-во символов - 200")
    private  String description;

    private TaskStatus status = TaskStatus.NOT_STARTED;

    @NotNull(message = "ID пользователя не должно быть пустым")
    private Long userId;

    public TaskCreateRequest(String title, String description, Long userId) {
        this.title = title;
        this.description = description;
        this.userId = userId;
    }

    public TaskCreateRequest() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
