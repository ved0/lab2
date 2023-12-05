package com.example.lab2.category;

import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends ListCrudRepository<Category, String> {
    List<Category> findBy();
    Optional<Category> findCategoryByName(String name);

    Optional<Category> findCategoryById(int id);
}
