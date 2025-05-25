package com.example.vehiclelogprocessing.dtos;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VehicleTelemetryDto {

    private final String recordId;

    private String source;

    private List<LogEntryDto> logEntries;

    public String getRecordId() {
        return recordId;
    }

    public String getSource() {
        return source;
    }

    public List<LogEntryDto> getLogEntries() {
        return logEntries;
    }

    @JsonCreator
    public VehicleTelemetryDto(@JsonProperty("record_id") String recordId, @JsonProperty("source") String source,
            @JsonProperty("log_entries") List<LogEntryDto> logEntries) {
        this.recordId = recordId;
        this.source = source;
        this.logEntries = logEntries;
    }
}
