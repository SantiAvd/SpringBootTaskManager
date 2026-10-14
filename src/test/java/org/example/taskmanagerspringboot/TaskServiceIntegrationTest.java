package org.example.taskmanagerspringboot;
import jakarta.servlet.ServletContext;
import org.example.taskmanagerspringboot.Entity.Task;
import org.example.taskmanagerspringboot.exceptions.UserNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import  org.springframework.test.context.ActiveProfiles;
import org.example.taskmanagerspringboot.Entity.User;
import org.example.taskmanagerspringboot.Service.TaskService;
import org.example.taskmanagerspringboot.Service.dto.requests.TaskCreateRequest;
import org.example.taskmanagerspringboot.Service.dto.response.TaskResponse;
import org.example.taskmanagerspringboot.repository.TaskRepository;
import org.example.taskmanagerspringboot.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;


import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest
class TaskServiceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;


    @PersistenceContext
    private EntityManager entityManager;
    @Test
    void applicationConnectsToDatabase() {
        Integer result = jdbcTemplate.queryForObject(
                "SELECT 1",
                Integer.class
        );

        assertTrue(result != null && result == 1);
    }

    @Test
    void createTaskForExistingUser() {
        User user = new User();
        user.setUserName("integration_test_user");
        user.setEmail("integration-test@example.com");

        user = userRepository.saveAndFlush(user);

        TaskCreateRequest request = new TaskCreateRequest(
                "Integration test task",
                "Check persistence in PostgreSQL",
                user.getId()
        );

        TaskResponse response = taskService.createTask(request);

        assertTrue(response.getId() != null);
        assertTrue(taskRepository.findById(response.getId()).isPresent());
    }


    @Test
    @Transactional
    void createTaskWithUser() {
        User user = userRepository.saveAndFlush(
                new User("integration_user_2", "integration2@example.com")
        );

        TaskCreateRequest request = new TaskCreateRequest(
                "Integration test task",
                "Check persistence in PostgreSQL",
                user.getId()
        );

        TaskResponse response = taskService.createTask(request);

        entityManager.flush();
        entityManager.clear();

        Task savedTask = taskRepository.findById(response.getId())
                .orElseThrow();

        assertEquals("Integration test task", savedTask.getTitle());
        assertEquals("Check persistence in PostgreSQL", savedTask.getDescription());
        assertEquals(user.getId(), savedTask.getUser().getId());
    }

    @Transactional
    @Test
    void createTaskWitoutUser() {
        TaskCreateRequest request = new TaskCreateRequest("Bahama", "mamam", 999L);

        Long countBefore = taskRepository.count();
        UserNotFoundException ex = Assertions.assertThrowsExactly(UserNotFoundException.class,
                () -> taskService.createTask(request));

        Long countAfter = taskRepository.count();
        assertEquals("Пользователь по id - " + 999L + " не найден", ex.getMessage());
        assertEquals(countBefore, countAfter);
    }
}