package fr.clbd.fire.utils.google;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationDto {
    @JsonProperty("latLng")
    private LatLongDto latLng;

    public LocationDto(LatLongDto latLng) {
        this.latLng = latLng;
    }

    public LocationDto() {
    }

    public LatLongDto getLatLong() {
        return latLng;
    }

    public void setLatLong(LatLongDto latLong) {
        this.latLng = latLong;
    }

    @Override
    public String toString() {
        return "LocationDto{" +
                "latLong=" + latLng +
                '}';
    }
}
