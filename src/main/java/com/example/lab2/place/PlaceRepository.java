package com.example.lab2.place;

import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PlaceRepository extends ListCrudRepository<Place,Integer> {
    List<Place> findBy();
    @Query("SELECT new com.example.lab2.place.PlaceDto(p.id, p.name, p.category.id, p.publicOrPrivate, p.lastUpdated, p.description, p.coordinates, p.created) " +
            "FROM Place p " +
            "WHERE p.publicOrPrivate = 'public'")
    List<PlaceDto> findAllPublicPlaces();
    @Query("SELECT new com.example.lab2.place.PlaceDto(p.id, p.name, p.category.id, p.publicOrPrivate, p.lastUpdated, p.description, p.coordinates, p.created) " +
            "FROM Place p " +
            "WHERE p.name = :name")
    Optional<PlaceDto> findByName(String name);

    @Query("SELECT new com.example.lab2.place.PlaceDto(p.id, p.name, p.category.id, p.publicOrPrivate, p.lastUpdated, p.description, p.coordinates, p.created) " +
            "FROM Place p " +
            "WHERE p.user.id = :userId OR p.publicOrPrivate = 'public'")
    List<PlaceDto> findAllUserAndPublicPlaces(int userId);

    @Query("SELECT new com.example.lab2.place.PlaceDto(p.id, p.name, p.category.id, p.publicOrPrivate, p.lastUpdated, p.description, p.coordinates, p.created) " +
            "FROM Place p " +
            "WHERE p.category.id = :categoryId AND p.publicOrPrivate = 'public'")
    List<PlaceDto> findAllPublicPlacesInCategory(int categoryId);

    @Query("SELECT new com.example.lab2.place.PlaceDto(p.id, p.name, p.category.id, p.publicOrPrivate, p.lastUpdated, p.description, p.coordinates, p.created) " +
            "FROM Place p " +
            "WHERE ST_Distance_Sphere(p.coordinates, :location) < :distance")
    List<PlaceDto> findPlacesAround(@Param("location") Point<G2D> location, @Param("distance") double distance);
}
