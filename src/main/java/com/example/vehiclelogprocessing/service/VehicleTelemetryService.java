package com.example.vehiclelogprocessing.service;

import com.example.vehiclelogprocessing.dtos.VehicleTelemetrySummaryDto;

public interface VehicleTelemetryService {

    VehicleTelemetrySummaryDto getVehicleTelemetrySummary(Long vehicleId);

}
