package com.example.lab2.category;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("api/categories")
public class CategoryController {
    CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @GetMapping
    public List<CategoryDto> getAll() {
        return service.getAllCategories();
    }

    @PostMapping
    public ResponseEntity<CategoryDto> insertCategory(@RequestBody @Valid CategoryDto category) {
        Optional<CategoryDto> insertedCategory = service.insertCategory(category);
        if (insertedCategory.isPresent()) {
            URI path = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(insertedCategory.get().id())
                    .toUri();
            return ResponseEntity.created(path).body(insertedCategory.get());
        }
        throw new NoSuchElementException("Something went wrong, the category not inserted");
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getOne(@PathVariable int id) {
        var category = service.getOneCategory(id);
        return category.map(categoryDto ->
                ResponseEntity.ok().body(categoryDto)).orElseGet(() ->
                ResponseEntity.notFound().build());
    }
}
