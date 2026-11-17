package com.epam.jmp.service.api;

import com.epam.jmp.domain.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    User createUser(User user);
    User updateUser(User user);
    void deleteUser(Long id);
    Optional<User> getUser(Long id);
    List<User> getAllUsers();
}
