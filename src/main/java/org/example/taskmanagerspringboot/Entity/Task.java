package org.example.taskmanagerspringboot.Entity;

import jakarta.persistence.*;
import org.example.taskmanagerspringboot.model.TaskPriority;
import org.example.taskmanagerspringboot.model.TaskStatus;

@Entity
@Table(name = "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus status = TaskStatus.NOT_STARTED;

    @Enumerated(EnumType.STRING)
    private TaskPriority priority = TaskPriority.NOT_ASSIGNED;

    @ManyToOne
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private User user;

    public Task(Long id, String title, String description) {
        this.title = title;
        this.description = description;
        this.id = id;
    }

    public Task() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}