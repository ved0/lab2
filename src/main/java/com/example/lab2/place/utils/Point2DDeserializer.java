package com.example.lab2.place.utils;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.IntNode;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Geometries;
import org.geolatte.geom.Point;

import java.io.IOException;

import static org.geolatte.geom.crs.CoordinateReferenceSystems.WGS84;

public class Point2DDeserializer extends JsonDeserializer<Point<G2D>> {

    @Override
    public Point<G2D> deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException, JacksonException {
        JsonNode node = jp.getCodec().readTree(jp);
        double lon = node.get("lon").asDouble();
        double lat = node.get("lat").asDouble();
        return Geometries.mkPoint(new G2D(lon, lat), WGS84);
    }
}
