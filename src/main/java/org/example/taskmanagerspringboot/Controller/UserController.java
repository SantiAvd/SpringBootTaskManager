package org.example.taskmanagerspringboot.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.taskmanagerspringboot.Service.UserService;
import org.example.taskmanagerspringboot.Service.dto.requests.UserCreateRequest;
import org.example.taskmanagerspringboot.Service.dto.response.UserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@Tag(
        name = "Users",
        description = "API для работы с пользователями"
)
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Создать пользователя",
            description = "Создает пользователя по параметрам имя и почта"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Успешное создание"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации")
    })
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserCreateRequest request) {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(userService.createUser(request));
    }
}
