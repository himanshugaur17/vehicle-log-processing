package com.example.vehiclelogprocessing.dtos;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ValueDto {
    @JsonProperty(value = "value")
    private final BigDecimal value;

    @JsonProperty(value = "unit")
    private final String unit;

    @JsonCreator
    public ValueDto(@JsonProperty("value") BigDecimal value, @JsonProperty("unit") String unit) {
        this.value = value;
        this.unit = unit;
    }

    public BigDecimal getValue() {
        return value;
    }

    public String getUnit() {
        return unit;
    }
}
