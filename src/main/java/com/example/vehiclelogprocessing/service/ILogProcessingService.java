package com.example.vehiclelogprocessing.service;

import com.example.vehiclelogprocessing.dtos.VehicleTelemetryDto;

public interface ILogProcessingService {

    VehicleTelemetryDto processLog(Long vehicleId);

}
