package org.example.taskmanagerspringboot.exceptions;

import org.example.taskmanagerspringboot.model.ErrorResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDTO handleResourceNotFound(ResourceNotFoundException exception) {
        log.warn("Ресурс не найден : {}",exception.getMessage());
        return new ErrorResponseDTO(
                    HttpStatus.NOT_FOUND,
                    exception.getMessage(),
                    null
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDTO handleNotBlanketRequest(MethodArgumentNotValidException exception) {
        Map<String, String> exceptionMap = new HashMap<String,String>();
        BindingResult br = exception.getBindingResult();

        for (FieldError error: br.getFieldErrors()) {
            exceptionMap.put(error.getField(), error.getDefaultMessage());
        }
        log.warn("Ошибка валидации");
        return new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST,
                exception.getMessage(),
                exceptionMap
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDTO handleIllegalArgumentRequest(IllegalArgumentException ex) {
        log.warn("Неверный аргумент запроса: {}", ex.getMessage());
        return new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                null
        );
    }
}
