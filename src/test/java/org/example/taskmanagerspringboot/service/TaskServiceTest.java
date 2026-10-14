package org.example.taskmanagerspringboot.service;

import org.example.taskmanagerspringboot.Entity.Task;
import org.example.taskmanagerspringboot.Entity.User;
import org.example.taskmanagerspringboot.Service.TaskService;
import org.example.taskmanagerspringboot.Service.UserService;
import org.example.taskmanagerspringboot.exceptions.TaskNotFoundException;
import org.example.taskmanagerspringboot.Service.dto.requests.TaskCreateRequest;
import org.example.taskmanagerspringboot.Service.dto.response.TaskResponse;
import org.example.taskmanagerspringboot.model.TaskPriority;
import org.example.taskmanagerspringboot.model.TaskStatus;
import org.example.taskmanagerspringboot.Service.dto.requests.TaskUpdateRequest;
import org.example.taskmanagerspringboot.repository.TaskRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @InjectMocks
    TaskService taskService;

    @Mock
    TaskRepository taskRepository;

    @Mock
    private UserService userService;

    Task task;
    Long id;
    TaskCreateRequest request;
    User user;


    @BeforeEach()
    void beforeEach(){
        task = new Task(1L,"Spring IDL", "fjgnpgdpb");
        user = new User("All", "jdnafi@mail.ru");
        user.setId(1L);
        id = 1L;
        user.addTasks(task);
        request = new TaskCreateRequest(task.getTitle(), task.getDescription(), user.getId());
    }

    @Test
    void getByIdTest() {
        when(taskRepository.findById(id)).thenReturn(Optional.of(task));

        TaskResponse response = taskService.getTaskBy(id);

        Assertions.assertEquals(1,response.getId());
        Assertions.assertEquals("Spring IDL",response.getTitle());
        Assertions.assertEquals("fjgnpgdpb",response.getDescription());
        Assertions.assertEquals("NOT_ASSIGNED",response.getPriority().toString());
        Assertions.assertEquals("NOT_STARTED",response.getStatus().toString());
    }


    @Test
    void  getByIdTaskNotFoundTest() {
        when(taskRepository.findById(id)).thenReturn(Optional.empty());

        TaskNotFoundException exception = Assertions.assertThrowsExactly(TaskNotFoundException.class,
                () -> taskService.getTaskBy(id));

        Assertions.assertEquals("Задачи под номером " + id + " не существует!", exception.getMessage());
        verify(taskRepository, times(1)).findById(id);
    }

    @Test
    void  getByIdIllegalArgumentTest() {
        IllegalArgumentException exception = Assertions.assertThrowsExactly(IllegalArgumentException.class,
                () -> taskService.getTaskBy(0L));

        Assertions.assertEquals("ID должен быть больше 0", exception.getMessage());
        verify(taskRepository, never()).findById(any());
    }

    @Test
    void createTaskTest() {
        when(userService.getUserById(user.getId())).thenReturn(user);
        TaskResponse response = taskService.createTask(request);

        Assertions.assertEquals("Spring IDL",response.getTitle());
        Assertions.assertEquals("fjgnpgdpb",response.getDescription());
        Assertions.assertEquals("NOT_ASSIGNED",response.getPriority().toString());
        Assertions.assertEquals("NOT_STARTED",response.getStatus().toString());

        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void updateTaskTest() {
        when(taskRepository.findById(id)).thenReturn(Optional.of(task));

        TaskUpdateRequest updateRequest = new TaskUpdateRequest("Miyagi Andy whole lifeefe", task.getDescription(), TaskStatus.NOT_STARTED, TaskPriority.LOW);
        TaskResponse response = taskService.updateTaskBy(id, updateRequest);

        Assertions.assertEquals(1,response.getId());
        Assertions.assertEquals("Miyagi Andy whole lifeefe",response.getTitle());
        Assertions.assertEquals("fjgnpgdpb",response.getDescription());
        Assertions.assertEquals(TaskPriority.LOW,response.getPriority());
        Assertions.assertEquals(TaskStatus.NOT_STARTED,response.getStatus());


        verify(taskRepository, times(1)).findById(id);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void updateTaskNotFoundTest() {
        when(taskRepository.findById(id)).thenReturn(Optional.empty());
        TaskUpdateRequest updateRequest = new TaskUpdateRequest("fjgnpgdpb", "Miyagi Andy whole lifeefe", TaskStatus.NOT_STARTED, TaskPriority.LOW);

        TaskNotFoundException ex = Assertions.assertThrowsExactly(TaskNotFoundException.class,
                () -> taskService.updateTaskBy(id, updateRequest));

        Assertions.assertEquals("Задачи под номером " + id + " не существует!", ex.getMessage());
        verify(taskRepository, times(1)).findById(id);
    }

    @Test
    void getAllTask() {
        Task task2 = new Task(2L, "Spring IDL", "slkf");
        Task task3 = new Task(3L, "Miyagi Andy whole lifeefe", "fjgnpgdpb");
        Task task4 = new Task(4L, "Spring communiti", "fjgnpgdpb");
        Task task5 = new Task(5L, "Spring DATA JPA",    "qrw");
        user.addTasks(task2);
        user.addTasks(task3);
        user.addTasks(task4);
        user.addTasks(task5);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Task> page = new PageImpl<>(List.of(task,task2,task3, task4, task5));

        when(taskRepository.findAll(pageable)).thenReturn(page);

        Page<TaskResponse> responses = taskService.getAllTask(null,pageable);

        Assertions.assertEquals(5, responses.getTotalElements());
        Assertions.assertEquals("Spring IDL", responses.getContent().get(0).getTitle());
        Assertions.assertEquals("Spring DATA JPA", responses.getContent().get(4).getTitle());

        verify(taskRepository, never()).findByTitleContaining(anyString(), any(Pageable.class));
        verify(taskRepository, times(1)).findAll(pageable);
    }

    @Test
    void getAllContainingTask() {
        Task task2 = new Task(2L, "Spring IDL", "slkf");
        Task task4 = new Task(4L, "Spring communiti", "fjgnpgdpb");
        Task task5 = new Task(5L, "Spring DATA JPA",   "qrw");

        user.addTasks(task2);
        user.addTasks(task4);
        user.addTasks(task5);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Task> page = new PageImpl<>(List.of(task,task2,task4, task5));

        when(taskRepository.findByTitleContaining("Spring", pageable)).thenReturn(page);

        Page<TaskResponse> responses = taskService.getAllTask("Spring",pageable);

        Assertions.assertEquals(4, responses.getTotalElements());
        Assertions.assertEquals("Spring IDL", responses.getContent().get(0).getTitle());
        Assertions.assertEquals("Spring DATA JPA", responses.getContent().get(3).getTitle());

        verify(taskRepository).findByTitleContaining("Spring", pageable);
        verify(taskRepository,never()).findAll(any(Pageable.class));
    }

    @Test
    void deleteTaskIsExist() {
        when(taskRepository.existsById(id))
                .thenReturn(true);

        taskService.deleteTaskBy(id);

        verify(taskRepository).existsById(id);
        verify(taskRepository).deleteById(id);
    }

    @Test
    void deleteTaskIsNotExist() {
        when(taskRepository.existsById(id))
                .thenReturn(false);

       TaskNotFoundException ex = Assertions.assertThrowsExactly(TaskNotFoundException.class, () -> taskService.deleteTaskBy(id));
        Assertions.assertEquals("Задачи под номером " + id + " не существует!", ex.getMessage());
        verify(taskRepository).existsById(id);
        verify(taskRepository, never()).deleteById(id);
    }


}
