package fr.clbd.fire.utils.google;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoutesDto {

    @JsonProperty("legs")
    private LegsDto[] legs;

    public RoutesDto(LegsDto[] legs) {
        this.legs = legs;
    }

    public RoutesDto() {
    }

    @Override
    public String toString() {
        return "RoutesDto{" +
                "legs=" + legs +
                '}';
    }

    public LegsDto[] getLegs() {
        return legs;
    }
}
