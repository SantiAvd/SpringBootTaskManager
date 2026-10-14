package org.example.taskmanagerspringboot.Service.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.example.taskmanagerspringboot.model.TaskPriority;
import org.example.taskmanagerspringboot.model.TaskStatus;

public class TaskUpdateRequest {
    @Schema(
            description = "Новое название задачи",
            example = "Подстричь собаку"
    )
    @NotBlank(message = "Обязательное поле")

    @Size(min = 3, max = 100, message = "Размер строки от 3 до 100 символов")
    private String title;

    @Schema(
            description = "Новое описание задачи",
            example = "Подстричь собаку сделать ей легкий фейд с задней части спереди оставить челку...."
    )
    @Size(max = 200, min = 2, message = "Размер строки от 2 до 200 символов")
    private String description;

    @NotNull
    private TaskStatus status;

    @NotNull
    private TaskPriority priority;

    public TaskUpdateRequest(String title, String description, TaskStatus status, TaskPriority priority) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;

    }

    public TaskUpdateRequest() {}

    public TaskPriority getPriority() {
        return priority;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
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
