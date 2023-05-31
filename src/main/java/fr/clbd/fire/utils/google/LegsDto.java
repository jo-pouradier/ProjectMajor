package fr.clbd.fire.utils.google;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
@JsonIgnoreProperties(ignoreUnknown = true)
public class LegsDto {
    @JsonProperty("steps")

    private StepDto[] steps;

    public LegsDto(StepDto[] steps) {
        this.steps = steps;
    }

    public LegsDto() {
    }

    @Override
    public String toString() {
        return "LegsDto{" +
                "steps=" + Arrays.toString(steps) +
                '}';
    }

    public StepDto[] getSteps() {
        return steps;
    }
}
