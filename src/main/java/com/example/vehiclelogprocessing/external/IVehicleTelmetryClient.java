package com.example.vehiclelogprocessing.external;

import com.example.vehiclelogprocessing.dtos.VehicleTelemetryDto;
import com.example.vehiclelogprocessing.exception.ExternalServiceException;

public interface IVehicleTelmetryClient {
    VehicleTelemetryDto getVehicleTelemetry(Long vehicleId) throws ExternalServiceException;

}
