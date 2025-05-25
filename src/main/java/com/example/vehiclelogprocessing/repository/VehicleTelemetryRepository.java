package com.example.vehiclelogprocessing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.vehiclelogprocessing.entity.VehicleTelemetryEntity;

@Repository
public interface VehicleTelemetryRepository extends JpaRepository<VehicleTelemetryEntity, Long> {
    @Query("SELECT v FROM VehicleTelemetryEntity v ORDER BY v.vehicle.id ASC")
    List<VehicleTelemetryEntity> findAllSortedByVehicleId();
}
