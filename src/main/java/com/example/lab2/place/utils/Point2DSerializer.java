package com.example.lab2.place.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;

public class Point2DSerializer extends JsonSerializer<Point<G2D>> {

    @Override
    public void serialize(Point<G2D> point, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("latitude", point.getPosition().getCoordinate(1));
        jsonGenerator.writeNumberField("longitude", point.getPosition().getCoordinate(0));
        jsonGenerator.writeEndObject();
    }
}
