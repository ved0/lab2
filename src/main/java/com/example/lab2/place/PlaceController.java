package com.example.lab2.place;

import com.example.lab2.exceptions.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/places")
public class PlaceController {
    PlaceService service;

    public PlaceController(PlaceService service) {
        this.service = service;
    }

    @GetMapping
    public List<PlaceDto> getAll(@RequestParam(required = false,
            defaultValue = "0") double lat, @RequestParam(required = false, defaultValue = "0") double lng,
                                 @RequestParam(required = false, defaultValue = "0") double dist) {
        return service.getAllPlaces(lat, lng, dist);
    }

    @PostMapping
    public ResponseEntity<PlaceDto> insertPlace(@RequestBody @Valid PlaceDto place) {
        Optional<PlaceDto> insertedPlace = service.addPlace(place);
        if (insertedPlace.isPresent()) {
            URI path = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(insertedPlace.get().id())
                    .toUri();
            return ResponseEntity.created(path).body(insertedPlace.get());
        } else {
            throw new ResourceNotFoundException("", place.name());
        }
    }

    @GetMapping("{id}")
    public PlaceDto getOnePlace(@PathVariable int id) {
        Optional<PlaceDto> place = service.getOnePlace(id);
        if (place.isPresent()) {
            return place.get();
        } else {
            throw new ResourceNotFoundException("", Integer.toString(id));
        }
    }

    @PatchMapping("{id}")
    public PlaceDto updateOnePlace(@PathVariable int id, @RequestBody PlaceDto update) {
        Optional<PlaceDto> place = service.updateOnePlace(id, update);
        if (place.isPresent()) {
            return place.get();
        } else {
            throw new ResourceNotFoundException("", Integer.toString(id));
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deletePlace(@PathVariable int id) {
        service.deleteOnePlace(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/category")
    public List<PlaceDto> findByCategory(@RequestParam int id) {
        return service.getAllPublicPlacesInCategory(id);
    }

}
