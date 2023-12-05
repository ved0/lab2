package com.example.lab2.user;

import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends ListCrudRepository<User, Integer> {
    List<User> findBy();

    Optional<User> findByUsername(String username);
}
