package com.example.lab2.place.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class InstantSerializer extends JsonSerializer<Instant> {
    @Override
    public void serialize(Instant instant, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        ZoneId localZone = ZoneId.systemDefault();
        ZonedDateTime localDateTime = instant.atZone(localZone);

        String formattedDateTime = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(localDateTime);

        jsonGenerator.writeString(formattedDateTime);
    }
}
