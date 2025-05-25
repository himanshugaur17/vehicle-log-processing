package com.example.vehiclelogprocessing.dtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TelemetryDto {
    @JsonProperty(value = "odometer")
    private final ValueDto odometer;

    @JsonProperty(value = "fuel")
    private final ValueDto fuel;

    @JsonCreator
    public TelemetryDto(@JsonProperty("odometer") ValueDto odometer, @JsonProperty("fuel") ValueDto fuel) {
        this.odometer = odometer;
        this.fuel = fuel;
    }

    public ValueDto getOdometer() {
        return odometer;
    }

    public ValueDto getFuel() {
        return fuel;
    }
}
