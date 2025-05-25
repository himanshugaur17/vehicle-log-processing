package com.example.vehiclelogprocessing.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.vehiclelogprocessing.dtos.LogEntryDto;
import com.example.vehiclelogprocessing.dtos.VehicleTelemetryDto;
import com.example.vehiclelogprocessing.dtos.responses.VehicleTelemetryIngestionResponse;
import com.example.vehiclelogprocessing.entity.VehicleEntity;
import com.example.vehiclelogprocessing.entity.VehicleTelemetryEntity;
import com.example.vehiclelogprocessing.mapper.TelemetryMapper;
import com.example.vehiclelogprocessing.repository.VehicleRepository;
import com.example.vehiclelogprocessing.repository.VehicleTelemetryRepository;

@Service
public class LogPersistanceService {
    private final VehicleRepository vehicleRepository;
    private final VehicleTelemetryRepository vehicleTelemetryRepository;
    private final TelemetryMapper telemetryMapper;

    public LogPersistanceService(@Autowired VehicleRepository vehicleRepository,
            @Autowired VehicleTelemetryRepository vehicleTelemetryRepository,
            @Autowired TelemetryMapper telemetryMapper) {
        this.vehicleRepository = vehicleRepository;
        this.vehicleTelemetryRepository = vehicleTelemetryRepository;
        this.telemetryMapper = telemetryMapper;
    }

    public VehicleTelemetryIngestionResponse ingestVehicleTelemetry(VehicleTelemetryDto vehicleTelemetryDto) {
        List<LogEntryDto> logEntries = vehicleTelemetryDto.getLogEntries();

        List<VehicleTelemetryEntity> vehicleTelemetries = logEntries.stream().map(logEntry -> {
            Long vehicleId = logEntry.getDevice().getVehicleId();
            VehicleEntity vehicle = vehicleRepository.getReferenceById(vehicleId);
            VehicleTelemetryEntity vehicleTelemetry = telemetryMapper.toEntity(logEntry.getTelemetry());
            vehicleTelemetry.setVehicle(vehicle);
            return vehicleTelemetry;
        }).collect(Collectors.toList());

        List<VehicleTelemetryEntity> savedTelemetries = vehicleTelemetryRepository.saveAll(vehicleTelemetries);

        return new VehicleTelemetryIngestionResponse(savedTelemetries.stream().map(VehicleTelemetryEntity::getId)
                .collect(Collectors.toList()));

    }
}
