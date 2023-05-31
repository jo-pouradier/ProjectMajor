package fr.clbd.fire.utils.google;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.model.dto.Coord;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LatLongDto {

    @JsonProperty("latitude")
    private double latitude;
    @JsonProperty("longitude")
    private double longitude;

    public LatLongDto(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public LatLongDto() {
    }

    @Override
    public String toString() {
        return "LatLongDto{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }


    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public Coord toCoord() {
        return new Coord(this.longitude, this.latitude);
    }
}