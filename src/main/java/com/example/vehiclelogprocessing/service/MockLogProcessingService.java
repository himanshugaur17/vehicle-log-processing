package com.example.vehiclelogprocessing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.example.vehiclelogprocessing.dtos.VehicleTelemetryDto;
import com.example.vehiclelogprocessing.exception.ExternalServiceException;
import com.example.vehiclelogprocessing.external.VehicleTelemetryClient;

@Service
@Qualifier("mockLogProcessingService")
public class MockLogProcessingService implements ILogProcessingService {
    private final VehicleTelemetryClient vehicleTelemetryClient;

    public MockLogProcessingService(@Autowired VehicleTelemetryClient vehicleTelemetryClient) {
        this.vehicleTelemetryClient = vehicleTelemetryClient;
    }

    @Override
    public VehicleTelemetryDto processLog(Long vehicleId) {
        VehicleTelemetryDto vehicleTelemetryDto = null;
        try {
            vehicleTelemetryDto = vehicleTelemetryClient.getVehicleTelemetry(vehicleId);
        } catch (ExternalServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return vehicleTelemetryDto;
    }

}
