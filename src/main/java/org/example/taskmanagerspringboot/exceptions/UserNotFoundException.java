package org.example.taskmanagerspringboot.exceptions;

public class UserNotFoundException extends ResourceNotFoundException {

    public UserNotFoundException(Long id) {
        super("Пользователь по id - " + id + " не найден");
    }
}
