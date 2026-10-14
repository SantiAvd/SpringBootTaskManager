package org.example.taskmanagerspringboot.exceptions;

public class TaskNotFoundException extends ResourceNotFoundException {
    public TaskNotFoundException(Long id) {
        super("Задачи под номером " + id + " не существует!");
    }
}
