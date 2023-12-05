package com.example.lab2.user;

import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserService {
    UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public int getUserIdForUsername(String username) {
        Optional<User> user = repository.findByUsername(username);
        if (user.isPresent()) return user.get().getId();
        throw new NoSuchElementException("User with username " + username + " not found");
    }

    public Optional<UserDto> getOneUser(int userId) {
        return map(repository.findById(userId));
    }

    static Optional<UserDto> map(Optional<User> user) {
        if (user.isEmpty()) {
            return Optional.empty();
        } else {
            var tempUser = user.get();
            return Optional.of(
                    new UserDto(tempUser.getId(), tempUser.getUsername(), tempUser.getFirstName(), tempUser.getLastName()));
        }
    }
}
