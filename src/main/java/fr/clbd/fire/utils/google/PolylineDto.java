package fr.clbd.fire.utils.google;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PolylineDto {
    @JsonProperty("encodedPolyline")
    String encodedPolyline;


    public PolylineDto(String encodedPolyline) {
        this.encodedPolyline = encodedPolyline;
    }

    public PolylineDto() {
    }

    public String getEncodedPolyline() {
        return encodedPolyline;
    }

}
