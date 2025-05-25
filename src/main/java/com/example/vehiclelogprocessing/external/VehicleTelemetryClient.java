package com.example.vehiclelogprocessing.external;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpConnectTimeoutException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.vehiclelogprocessing.dtos.VehicleTelemetryDto;
import com.example.vehiclelogprocessing.exception.ExternalServiceException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class VehicleTelemetryClient implements IVehicleTelmetryClient {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String vehicleTelemetryUrl;

    public VehicleTelemetryClient(@Autowired HttpClient httpClient, @Autowired ObjectMapper objectMapper,
            @Value("${external.api.vehicle.telemetry.url}") String vehicleTelemetryUrl) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
        this.vehicleTelemetryUrl = vehicleTelemetryUrl;
    }

    @Override
    public VehicleTelemetryDto getVehicleTelemetry(Long vehicleId) throws ExternalServiceException {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(vehicleTelemetryUrl + vehicleId))
                .build();
        HttpResponse<String> response = null;
        try {
            response = httpClient.send(request, BodyHandlers.ofString());

        } catch (HttpConnectTimeoutException timeoutException) {
            throw new ExternalServiceException("connection timeout for external service", timeoutException);
        } catch (IOException e) {
            throw new ExternalServiceException("Network communication error with external service", e);
        } catch (InterruptedException e) {
            throw new ExternalServiceException(
                    "the network request was interrupted, likely due to application shutdown", e);
        }
        int statusCode = response.statusCode();
        if (statusCode < 200 || statusCode >= 300)
            throw new ExternalServiceException("External service failed with status code " + statusCode);

        try {
            VehicleTelemetryDto dto = objectMapper.readValue(response.body(), VehicleTelemetryDto.class);
            return dto;
        } catch (JsonProcessingException e) {
            throw new ExternalServiceException("Error parsing response from external service", e);
        }

    }
}
