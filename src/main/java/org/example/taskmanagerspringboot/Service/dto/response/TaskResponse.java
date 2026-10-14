package org.example.taskmanagerspringboot.Service.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.example.taskmanagerspringboot.model.TaskPriority;
import org.example.taskmanagerspringboot.model.TaskStatus;

public class TaskResponse {
    @Schema(
            description = "Уникальный идентификатор задачи",
            example = "1"
    )
    private Long id;

    @Schema(
            description = "Название задачи",
            example = "Высушить белье"
    )
    private String title;

    @Schema(
            description = "Описание задачи",
            example = "Высушить 10 маек 2 трусы и 5 пар носков"
    )
    private String description;

    @Schema(
            description = "Статус задачи",
            example = "NOT_STARTED,IN_PROGRESS,DONE"
    )
    private TaskStatus status;

    @Schema(
            description = "Приоритет задачи",
            example = "HIGH, NOT_ASSIGNED, LOW, MEDIUM, URGEN"
    )
    private TaskPriority priority;

    private Long user_id;

    public TaskResponse(Long id, String title, String description, TaskStatus status, TaskPriority priority, Long user_id) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.user_id = user_id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
