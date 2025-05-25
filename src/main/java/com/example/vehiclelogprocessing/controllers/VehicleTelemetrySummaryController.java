package com.example.vehiclelogprocessing.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.vehiclelogprocessing.dtos.VehicleTelemetrySummaryDto;
import com.example.vehiclelogprocessing.service.VehicleTelemetryService;

@RestController("/api/v1/vehicles/{vehicleId}/telemetry-summary")
public class VehicleTelemetrySummaryController {

    // private final VehicleTelemetryService vehicleTelemetryService;

    // public VehicleTelemetrySummaryController(@Autowired VehicleTelemetryService
    // vehicleTelemetryService) {
    // this.vehicleTelemetryService = vehicleTelemetryService;
    // }

    // @GetMapping
    // public ResponseEntity<VehicleTelemetrySummaryDto>
    // getVehicleTelemetrySummary(@PathVariable Long vehicleId) {
    // return
    // ResponseEntity.ok(vehicleTelemetryService.getVehicleTelemetrySummary(vehicleId));
    // }

}
