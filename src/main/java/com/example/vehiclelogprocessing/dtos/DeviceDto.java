package com.example.vehiclelogprocessing.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceDto {

    @JsonProperty(value = "vehicle_id")
    @NotNull
    private Long vehicleId;

    @JsonProperty(value = "model")
    @NotNull
    private String model;

    public Long getVehicleId() {
        return vehicleId;
    }

    public String getModel() {
        return model;
    }

}
