package com.example.lab2.category;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    public List<CategoryDto> getAllCategories() {
        return repository.findAll().stream()
                .map(category ->
                        new CategoryDto(
                                category.getId(),
                                category.getName(),
                                category.getSymbol(),
                                category.getDescription())).toList();
    }

    public Optional<CategoryDto> insertCategory(CategoryDto category) {
        if (repository.findCategoryByName(category.name()).isEmpty()) {
            Category categoryToInsert = new Category();
            categoryToInsert.setName(category.name());
            categoryToInsert.setSymbol(category.symbol());
            categoryToInsert.setDescription(category.description());
            return map(Optional.of(repository.save(categoryToInsert)));
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This category already exists");
        }
    }

    public Optional<CategoryDto> getOneCategory(int categoryId) {
        return map(repository.findCategoryById(categoryId));
    }

    static Optional<CategoryDto> map(Optional<Category> category) {
        if (category.isEmpty()) {
            return Optional.empty();
        } else {
            var tempCategory = category.get();
            return Optional.of(
                    new CategoryDto(tempCategory.getId(), tempCategory.getName(), tempCategory.getSymbol(), tempCategory.getDescription())
            );
        }
    }

}
