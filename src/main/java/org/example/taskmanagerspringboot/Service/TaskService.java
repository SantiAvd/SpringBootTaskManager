package org.example.taskmanagerspringboot.Service;

import org.example.taskmanagerspringboot.Entity.Task;
import org.example.taskmanagerspringboot.Entity.User;
import org.example.taskmanagerspringboot.exceptions.TaskNotFoundException;
import org.example.taskmanagerspringboot.Service.dto.requests.TaskCreateRequest;
import org.example.taskmanagerspringboot.Service.dto.response.TaskResponse;
import org.example.taskmanagerspringboot.Service.dto.requests.TaskUpdateRequest;
import org.example.taskmanagerspringboot.repository.TaskRepository;

import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.springframework.transaction.annotation.Transactional;


@Service
public class TaskService {
    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);
    private final TaskRepository taskRepository;
    private final UserService userService;

    public TaskService(TaskRepository taskRepository, UserService userService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
    }

    public TaskResponse getTaskBy(Long id) {
        if (id == null || id <= 0) {
            logger.warn("Некорректный ID задачи id={}", id);
            throw new IllegalArgumentException("ID должен быть больше 0");
        }

        Task task =  taskRepository.findById(id)
                      .orElseThrow(() -> new TaskNotFoundException(id));

        return new TaskResponse(task.getId(), task.getTitle(), task.getDescription(), task.getStatus(), task.getPriority(), task.getUser().getId());
    }

    @Transactional
    public TaskResponse createTask(TaskCreateRequest request) {
        User user = userService.getUserById(request.getUserId());
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        user.addTasks(task);
        taskRepository.save(task);

        return new TaskResponse(task.getId(), task.getTitle(),
                task.getDescription(), task.getStatus(), task.getPriority(), task.getUser().getId());
    }

    public Page<TaskResponse> getAllTask(String title, Pageable pageable) {

        if (title != null && !title.isBlank()) {

             return taskRepository.findByTitleContaining(title, pageable)
                     .map(task -> new TaskResponse(task.getId(),
                     task.getTitle(), task.getDescription(), task.getStatus(), task.getPriority(), task.getUser().getId()));
        }

        return taskRepository.findAll(pageable)
                .map(task -> new TaskResponse(task.getId(),
                        task.getTitle(), task.getDescription(), task.getStatus(), task.getPriority(), task.getUser().getId()));
    }

    @Transactional
    public TaskResponse updateTaskBy(Long id, TaskUpdateRequest request) {

        Task task = taskRepository.findById(id)
                        .orElseThrow(() -> new TaskNotFoundException(id));
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setPriority(request.getPriority());

        return new TaskResponse(task.getId(), task.getTitle(), task.getDescription(), task.getStatus(), task.getPriority(), task.getUser().getId());
    }

    @Transactional
    public void deleteTaskBy(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException(id);
        }

        taskRepository.deleteById(id);
    }

}
