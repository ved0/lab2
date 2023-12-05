package com.example.lab2.place;

import com.example.lab2.place.utils.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;

import java.io.Serializable;
import java.time.Instant;

public record PlaceDto(int id, @NotBlank String name, @NotNull @Min(1) int categoryId,
                       String publicOrPrivate,
                       @JsonSerialize(using = InstantSerializer.class) Instant lastUpdated, String description,
                       @JsonSerialize(using = Point2DSerializer.class)
                       @JsonDeserialize(using = Point2DDeserializer.class) Point<G2D> coordinates,
                       @JsonSerialize(using = InstantSerializer.class) Instant created) implements Serializable {
}
