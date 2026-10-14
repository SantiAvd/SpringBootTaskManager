package org.example.taskmanagerspringboot;

import com.jayway.jsonpath.JsonPath;
import org.example.taskmanagerspringboot.Entity.User;
import org.example.taskmanagerspringboot.Service.dto.requests.TaskCreateRequest;
import org.example.taskmanagerspringboot.repository.TaskRepository;
import org.example.taskmanagerspringboot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TaskControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    private User createUser() {
        return userRepository.save(
                new User("test-user", "test@example.com")
        );
    }

    @Test
    @Transactional
    void createTaskShouldReturn201() throws Exception {
        User user = createUser();

        TaskCreateRequest request =
                new TaskCreateRequest(
                        "Learn Spring",
                        "Practice integration tests",
                        user.getId()
                );
        String json = """
    {
        "title": "Learn Spring",
        "description": "Practice integration tests",
        "userId": %d
    }
    """.formatted(user.getId());


        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("Learn Spring"));
    }

    @Test
    @Transactional
    void getTasksShouldReturn200() throws Exception {
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    void getTaskShouldReturn404WhenTaskDoesNotExist() throws Exception {
        mockMvc.perform(get("/tasks/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void deleteTaskShouldReturn204() throws Exception {
        User user = createUser();

        TaskCreateRequest request =
                new TaskCreateRequest(
                        "Delete me",
                        "Task for deletion",
                        user.getId()
                );
        String json = """
    {
        "title": "%s",
        "description": "%s",
        "userId": %d
    }
    """.formatted(
                request.getTitle(),
                request.getDescription(),
                request.getUserId()
        );

        String response = mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Number taskIdValue = JsonPath.read(response, "$.id");
        long taskId = taskIdValue.longValue();

        mockMvc.perform(delete("/tasks/" + taskId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/tasks/" + taskId))
                .andExpect(status().isNotFound());
    }

    @Test
    void createTaskShouldReturn400ForInvalidRequest() throws Exception {
        String invalidJson = """
                {
                    "title": "",
                    "description": "Invalid task",
                    "userId": 1
                }
                """;

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }
}