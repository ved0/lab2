package com.example.lab2.category;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;

public record CategoryDto(int id, @Size(max = 255) @NotEmpty @NotNull String name,
                          @Size(max = 255) @NotEmpty @NotNull String symbol,
                          @Size(max = 255) @NotEmpty @NotNull String description) implements Serializable {
}
