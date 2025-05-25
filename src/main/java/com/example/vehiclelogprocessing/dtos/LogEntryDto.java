package com.example.vehiclelogprocessing.dtos;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LogEntryDto {
    @NotBlank
    @JsonProperty(value = "event_id")
    private String eventId;

    @NotNull
    @JsonProperty(value = "timestamp")
    private Instant timestamp;

    @NotNull
    @JsonProperty(value = "event_type")
    private EventType eventType;

    @NotNull
    @JsonProperty(value = "telemetry")
    private TelemetryDto telemetry;

    @JsonProperty(value = "device")
    private DeviceDto device;

    public LogEntryDto() {
    }

    public String getEventId() {
        return eventId;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public EventType getEventType() {
        return eventType;
    }

    public TelemetryDto getTelemetry() {
        return telemetry;
    }

    public DeviceDto getDevice() {
        return device;
    }
}
