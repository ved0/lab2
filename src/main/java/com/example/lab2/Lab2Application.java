package com.example.lab2;

import org.geolatte.geom.G2D;
import org.geolatte.geom.crs.CoordinateReferenceSystem;
import org.geolatte.geom.json.GeolatteGeomModule;
import org.hibernate.spatial.GeolatteGeometryJavaType;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import static org.geolatte.geom.crs.CoordinateReferenceSystems.WGS84;

@SpringBootApplication
public class Lab2Application {

    public static void main(String[] args) {
        SpringApplication.run(Lab2Application.class, args);
    }

    @Bean
    GeolatteGeomModule geolatteGeomModule(){
        CoordinateReferenceSystem<G2D> crs = WGS84;
        return new GeolatteGeomModule(crs);
    }
}
