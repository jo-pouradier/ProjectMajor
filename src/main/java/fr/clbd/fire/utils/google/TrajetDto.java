package fr.clbd.fire.utils.google;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrajetDto {

    @JsonProperty("routes")
    private RoutesDto[] routes;

    public TrajetDto(RoutesDto[] routes) {
        this.routes = routes;
    }

    public TrajetDto() {
    }

    @Override
    public String toString() {
        return "TrajetDto{" +
                "routes=" + Arrays.toString(routes) +
                '}';
    }

    public RoutesDto[] getRoutes() {
        return routes;
    }
}