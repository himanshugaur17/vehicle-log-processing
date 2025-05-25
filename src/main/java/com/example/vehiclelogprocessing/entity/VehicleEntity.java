package com.example.vehiclelogprocessing.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "vehicle")
public class VehicleEntity {
    @Id
    private Long id;

    @Column(name = "owner", nullable = false, length = 100)
    @NotBlank
    private String owner;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<VehicleTelemetryEntity> telemetries;

    public Long getId() {
        return id;
    }

    public String getOwner() {
        return owner;
    }

    public List<VehicleTelemetryEntity> getTelemetries() {
        return telemetries;
    }

    public VehicleEntity(Long id, String owner) {
        this.id = id;
        this.owner = owner;
        this.telemetries = new ArrayList<>();
    }

    public VehicleEntity() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        VehicleEntity vehicle = (VehicleEntity) o;
        return id != null && id.equals(vehicle.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

}