package com.example.vehiclelogprocessing.entity;

import java.math.BigDecimal;
import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "vehicle_telemetry", indexes = {
        @Index(name = "idx_vehicle_timestamp", columnList = "vehicle_id, timestamp DESC", unique = true) })
@NoArgsConstructor
public class VehicleTelemetryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "timestamp", nullable = false, updatable = false)
    private Instant timestamp;
    @Column(name = "odometer_km", precision = 10, scale = 2, updatable = false)
    private BigDecimal odometerKm;

    @DecimalMax(value = "1.0", message = "fuel percentage can't be greater than 1.")
    @Column(name = "fuel_percent", precision = 5, scale = 4)
    private BigDecimal fuelPercent;

    @JoinColumn(name = "vehicle_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private VehicleEntity vehicle;

    public VehicleTelemetryEntity() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public BigDecimal getOdometerKm() {
        return odometerKm;
    }

    public void setOdometerKm(BigDecimal odometerKm) {
        this.odometerKm = odometerKm;
    }

    public BigDecimal getFuelPercent() {
        return fuelPercent;
    }

    public void setFuelPercent(BigDecimal fuelPercent) {
        this.fuelPercent = fuelPercent;
    }

    public VehicleEntity getVehicle() {
        return vehicle;
    }

    public void setVehicle(VehicleEntity vehicle) {
        this.vehicle = vehicle;
    }

    @PrePersist
    private void onPrePersist() {
        if (timestamp == null)
            timestamp = Instant.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        VehicleTelemetryEntity that = (VehicleTelemetryEntity) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
