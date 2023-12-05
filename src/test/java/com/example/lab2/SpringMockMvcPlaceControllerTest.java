package com.example.lab2;

import com.example.lab2.place.PlaceController;
import com.example.lab2.place.PlaceDto;
import com.example.lab2.place.PlaceRepository;
import com.example.lab2.place.PlaceService;
import com.example.lab2.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Geometries;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Optional;

import static org.geolatte.geom.crs.CoordinateReferenceSystems.WGS84;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PlaceController.class)
@Import(SecurityConfig.class)
public class SpringMockMvcPlaceControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    private PlaceService service;

    @Test
    void shouldReturnPlaceDtoWithStatus200() throws Exception {
        Instant now = Instant.now();
        PlaceDto expected = new PlaceDto(1, "some place", 1, "public", now, "blabla", Geometries.mkPoint(new G2D(30, 30), WGS84), now);

        when(service.getOnePlace(1)).thenReturn(Optional.of((new PlaceDto(1, "some place", 1, "public", now, "blabla", Geometries.mkPoint(new G2D(30, 30), WGS84), now))));

        mockMvc.perform(get("/api/places/1")).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(content().json(mapper.writeValueAsString(expected)));
    }

    @Test
    @WithAnonymousUser
    void unauthorizedAddingOfPlaceShouldReturn401() throws Exception {
        mockMvc.perform(post("/api/places")).andExpect(status().isUnauthorized());
    }

    /*
    @Test
    @WithMockUser(username = "cool_kid", roles = "USER")
    void authorizedAddingOfPlaceShouldReturn201AndTheCreatedPlaceDto() throws Exception {
        Instant now = Instant.now(Clock.fixed(
                Instant.parse("2018-08-22T10:00:00Z"),
                ZoneOffset.UTC));
        PlaceDto placeToAdd = new PlaceDto(1, "some place", 1, "public", now, "blabla", Geometries.mkPoint(new G2D(30, 30), WGS84), now);

        when(service.addPlace(placeToAdd)).thenReturn(Optional.of(new PlaceDto(1, "some place", 1, "public", now, "blabla", Geometries.mkPoint(new G2D(30, 30), WGS84), now)));

        mockMvc.perform(post("/api/places")
                    .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(placeToAdd)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/places/1"));
    }
    */
}
