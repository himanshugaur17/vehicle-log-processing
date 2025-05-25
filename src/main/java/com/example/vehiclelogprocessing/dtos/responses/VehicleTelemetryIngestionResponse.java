package com.example.vehiclelogprocessing.dtos.responses;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VehicleTelemetryIngestionResponse {
    @JsonProperty(value = "ingested_telemetry_ids")
    private List<Long> ingestedTelemetryIds;

    public VehicleTelemetryIngestionResponse(List<Long> ingestedTelemetryIds) {
        this.ingestedTelemetryIds = new ArrayList<>(ingestedTelemetryIds);
    }

    public List<Long> getIngestedTelemetryIds() {
        return ingestedTelemetryIds;
    }
}
