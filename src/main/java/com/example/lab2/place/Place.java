package com.example.lab2.place;

import com.example.lab2.user.User;
import com.example.lab2.category.Category;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;

import java.time.Instant;

@Entity
@Setter
@Getter
@Table(name = "places")
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @NotBlank
    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "added_by_user_id")
    private User user;

    @NotNull
    @Column(name = "public_or_private", nullable = false)
    private String publicOrPrivate;

    @Column(name = "last_updated", nullable = false)
    private Instant lastUpdated;

    @Size(max = 255)
    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "coordinates", columnDefinition = "geometry(Point, 4326) not null")
    private Point<G2D> coordinates;

    @Column(name = "created", nullable = false)
    private Instant created;
}