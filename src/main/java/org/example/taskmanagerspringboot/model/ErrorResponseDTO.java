package org.example.taskmanagerspringboot.model;

import org.springframework.http.HttpStatus;

import java.util.Map;

public class ErrorResponseDTO {

    private int status;
    private String message;
    private Map<String, String> errors;

    public ErrorResponseDTO(HttpStatus status, String message, Map<String, String> errors) {
        this.status = status.value();
        this.message = message;
        this.errors = errors;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

}
