package fr.clbd.fire.utils.google;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonIgnoreProperties(ignoreUnknown = true)
public class StepDto {
    @JsonProperty("distanceMeters")
    private int distanceMeters;
    @JsonProperty("startLocation")
    private LocationDto startLocation;
    @JsonProperty("endLocation")
    private LocationDto endLocation;
    @JsonProperty("polyline")
    private PolylineDto polyline;

    public StepDto(int distanceMeters, LocationDto startLocation, LocationDto endLocation) {
        this.distanceMeters = distanceMeters;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
    }

    public StepDto() {
    }

    @Override
    public String toString() {
        return "StepDto{" +
                "distanceMeters=" + distanceMeters +
                ", startLocation=" + startLocation +
                ", endLocation=" + endLocation +
                '}';
    }

    public int getDistanceMeters() {
        return distanceMeters;
    }

    public LocationDto getStartLocation() {
        return startLocation;
    }

    public LocationDto getEndLocation() {
        return endLocation;
    }

    public PolylineDto getPolyline(){
        return polyline;
    }

}
