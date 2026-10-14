package org.example.taskmanagerspringboot.exceptions;

public class UsernameNotFoundException extends ResourceNotFoundException {
    public UsernameNotFoundException(String username) {
        super("Пользователя с логином " + username + " не существует!");
    }
}
