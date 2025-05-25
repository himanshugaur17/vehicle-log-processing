package com.example.vehiclelogprocessing.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.vehiclelogprocessing.dtos.VehicleTelemetryDto;
import com.example.vehiclelogprocessing.dtos.responses.VehicleTelemetryIngestionResponse;
import com.example.vehiclelogprocessing.service.ILogProcessingService;
import com.example.vehiclelogprocessing.service.LogPersistanceService;

@RestController
@RequestMapping("/api/v1/vehicles/{vehicleId}/logs")
public class VehicleLogProcessingController {
    private final ILogProcessingService logProcessingService;
    private final LogPersistanceService logPersistanceService;

    public VehicleLogProcessingController(@Autowired ILogProcessingService logProcessingService,
            @Autowired LogPersistanceService logPersistanceService) {
        this.logProcessingService = logProcessingService;
        this.logPersistanceService = logPersistanceService;
    }

    @PostMapping
    public ResponseEntity<VehicleTelemetryIngestionResponse> createVehicleLogSummary(@PathVariable Long vehicleId) {
        VehicleTelemetryDto vehicleTelemetryDto = logProcessingService.processLog(vehicleId);
        VehicleTelemetryIngestionResponse vehicleTelemetryIngestionResponse = logPersistanceService
                .ingestVehicleTelemetry(vehicleTelemetryDto);
        return ResponseEntity.ok(vehicleTelemetryIngestionResponse);
    }

}
