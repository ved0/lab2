package com.example.lab2.category;

import com.example.lab2.category.utils.EmojiSymbol;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name")
    @Size(max = 255)
    private String name;
    @Size(max = 255)
    @Column(name = "symbol")
    @EmojiSymbol
    private String symbol;
    @Size(max = 255)
    @Column(name = "description")
    private String description;
}
