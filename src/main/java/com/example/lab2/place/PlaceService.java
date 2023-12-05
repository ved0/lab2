package com.example.lab2.place;

import com.example.lab2.user.User;
import com.example.lab2.user.UserDto;
import com.example.lab2.user.UserService;
import com.example.lab2.category.Category;
import com.example.lab2.category.CategoryDto;
import com.example.lab2.category.CategoryService;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;
import org.geolatte.geom.builder.DSL;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.geolatte.geom.builder.DSL.g;
import static org.geolatte.geom.crs.CoordinateReferenceSystems.WGS84;

@Service
public class PlaceService {
    PlaceRepository repository;
    CategoryService categoryService;
    UserService userService;

    public PlaceService(PlaceRepository repository, CategoryService categoryService, UserService userService) {
        this.repository = repository;
        this.categoryService = categoryService;
        this.userService = userService;
    }

    public Optional<PlaceDto> addPlace(PlaceDto place) {
        CategoryDto categoryDto = categoryService.getOneCategory(place.categoryId()).orElse(null);
        if (categoryDto == null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, place.categoryId() + " is not a valid ID for a category");
        }
        if (repository.findByName(place.name()).isEmpty()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (!(authentication instanceof AnonymousAuthenticationToken)) {
                int userId = userService.getUserIdForUsername(authentication.getName());
                Optional<UserDto> userDto = userService.getOneUser(userId);
                if (userDto.isPresent()) {
                    Place placeToAdd = createPlaceToAdd(place, categoryDto, userDto.get());
                    return map(repository.save(placeToAdd));
                }
            }
        }
        throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "This place is already in the database");
    }

    public List<PlaceDto> getAllPlaces(double latitude, double longitude, double distance) {
        if (distance == 0) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication instanceof AnonymousAuthenticationToken) {
                return getAllPublicPlaces();
            }
            return getAllPlacesForUser(authentication.getName());
        }
        return findPlacesAround(latitude, longitude, distance);
    }

    public List<PlaceDto> getAllPublicPlacesInCategory(int categoryId) {
        return repository.findAllPublicPlacesInCategory(categoryId);
    }

    public List<PlaceDto> getAllPublicPlaces() {
        return repository.findAllPublicPlaces();
    }

    public Optional<PlaceDto> updateOnePlace(int id, PlaceDto toUpdate) {
        Optional<Place> p = repository.findById(id);
        if (p.isPresent()) {
            p.get().setDescription(toUpdate.description());
            p.get().setPublicOrPrivate(toUpdate.publicOrPrivate());
            return map(repository.save(p.get()));
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid place id!");
    }

    public Optional<PlaceDto> getOnePlace(int id) {
        var placeOptional = repository.findById(id);
        if (placeOptional.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This place does not exists");
        Place place = placeOptional.get();
        if (place.getPublicOrPrivate().equalsIgnoreCase("public")) {
            return map(place);
        } else {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (!(authentication instanceof AnonymousAuthenticationToken)) {
                if (place.getUser().getUsername().equalsIgnoreCase(authentication.getName())) {
                    return map(place);
                } else {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You dont have access to this private place");
                }
            }
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You need to log in and own this to see it");
        }
    }

    public List<PlaceDto> getAllPlacesForUser(String name) {
        try {
            int userId = userService.getUserIdForUsername(name);
            return repository.findAllUserAndPublicPlaces(userId);
        } catch (NoSuchElementException e) {
            return getAllPublicPlaces();
        }
    }

    public void deleteOnePlace(int id) {
        Optional<Place> place = repository.findById(id);
        if (place.isPresent()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (!(authentication instanceof AnonymousAuthenticationToken)) {
                if (place.get().getUser().getUsername().equalsIgnoreCase(authentication.getName())) {
                    repository.deleteById(id);
                    return;
                }
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "This is not your place, so you cant delete it");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The place with this id doesnt exist! ");
        }
    }

    public List<PlaceDto> findPlacesAround(double lat, double lng, double distance) {
        if (lat < -90 || lat > 90 || lng < -180 || lng > 180) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Invalid latitude or longitude");
        }
        Point<G2D> location = DSL.point(WGS84, g(lng, lat));
        return repository.findPlacesAround(location, distance);
    }

    private static Optional <PlaceDto> map(Place place) {
        return Optional.of(new PlaceDto(
                place.getId(),
                place.getName(),
                place.getCategory().getId(),
                place.getPublicOrPrivate(),
                place.getLastUpdated(),
                place.getDescription(),
                place.getCoordinates(),
                place.getCreated()));
    }

    private static Place createPlaceToAdd(PlaceDto place, CategoryDto categoryDto, UserDto userDto) {
        Category category = new Category();
        category.setId(categoryDto.id());
        category.setName(categoryDto.name());
        category.setSymbol(categoryDto.symbol());
        category.setDescription(categoryDto.description());

        User user = new User();
        user.setId(userDto.id());
        user.setUsername(userDto.username());
        user.setFirstName(userDto.firstName());
        user.setLastName(userDto.lastName());

        Place placeToAdd = new Place();
        placeToAdd.setName(place.name());
        placeToAdd.setCategory(category);
        placeToAdd.setUser(user);
        placeToAdd.setPublicOrPrivate(place.publicOrPrivate() == null ? "public" : place.publicOrPrivate());
        placeToAdd.setDescription(place.description());
        placeToAdd.setCoordinates(place.coordinates());
        placeToAdd.setCreated(Instant.now());
        placeToAdd.setLastUpdated(Instant.now());
        return placeToAdd;
    }
}