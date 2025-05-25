package com.example.vehiclelogprocessing.mapper;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.example.vehiclelogprocessing.dtos.TelemetryDto;
import com.example.vehiclelogprocessing.entity.VehicleTelemetryEntity;

@Component
public class TelemetryMapper {

    public TelemetryDto toDto(VehicleTelemetryEntity vehicleTelemetry) {

        return null;
    }

    public VehicleTelemetryEntity toEntity(TelemetryDto dto) {
        VehicleTelemetryEntity entity = new VehicleTelemetryEntity();
        BigDecimal odometerInKm = convertToKm(dto.getOdometer().getValue(), dto.getOdometer().getUnit());
        entity.setOdometerKm(odometerInKm);
        entity.setFuelPercent(dto.getFuel().getValue().divide(BigDecimal.valueOf(100)));
        return entity;
    }

    private BigDecimal convertToKm(BigDecimal value, String unit) {
        if (unit.equals("km")) {
            return value;
        } else if (unit.equals("mi")) {
            return value.multiply(BigDecimal.valueOf(1.60934));
        } else {
            throw new IllegalArgumentException("Invalid unit: " + unit);
        }
    }

}
