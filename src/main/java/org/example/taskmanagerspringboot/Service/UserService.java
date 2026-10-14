package org.example.taskmanagerspringboot.Service;
import org.example.taskmanagerspringboot.Entity.User;
import org.example.taskmanagerspringboot.Service.dto.requests.UserCreateRequest;
import org.example.taskmanagerspringboot.Service.dto.response.UserResponse;
import org.example.taskmanagerspringboot.exceptions.UserNotFoundException;
import org.example.taskmanagerspringboot.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserById(Long id) {
        if (id <= 0) {
            log.warn("НЕкорректный id - {}", id);
            throw new IllegalArgumentException("ID должен быть больше 0");
        }

        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return user;
    }

    @Transactional
    public UserResponse createUser(UserCreateRequest request) {
        User user = new User();
        user.setUserName(request.getUsername());
        user.setEmail(request.getEmail());
        userRepository.save(user);
        return new UserResponse(user.getId(), user.getUserName(), user.getEmail());
    }

}
