package com.example.vehiclelogprocessing.external;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.file.Files;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.example.vehiclelogprocessing.exception.ExternalServiceException;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class VehicleTelemetryClientTest {
    @Mock
    private HttpClient httpClient;
    @Mock
    private ObjectMapper objectMapper;
    private VehicleTelemetryClient vehicleTelemetryClient;
    private static final String TEST_URL = "http://test-url";

    @BeforeEach
    void setUp() {
        vehicleTelemetryClient = new VehicleTelemetryClient(httpClient, objectMapper, TEST_URL);
    }

    void getVehicleTelemetry_Success() throws Exception {

    }

    @Test
    public void getVehicleTelemetry_NotFound() throws Exception {
        // given
        Long vehicleId = 1L;
        String expectedErrorMessage = "External service failed with status code 404";
        HttpResponse<String> mockResponse = mock(HttpResponse.class);
        when(mockResponse.statusCode()).thenReturn(404);

        when(httpClient.send(
                argThat(request -> request.uri().toString().equals(TEST_URL + "/" + vehicleId)),
                eq(BodyHandlers.ofString()))).thenReturn(mockResponse);

        // Act & Assert
        ExternalServiceException exception = assertThrows(
                ExternalServiceException.class,
                () -> vehicleTelemetryClient.getVehicleTelemetry(vehicleId));

        // Verify exception details
        Assertions.assertEquals(expectedErrorMessage, exception.getMessage());
        // Verify interactions
        Mockito.verify(httpClient, times(1)).send(any(HttpRequest.class), eq(BodyHandlers.ofString()));
        Mockito.verifyNoMoreInteractions(httpClient, objectMapper);
    }

    void getVehicleTelemetry_ServerError() {

    }

    void getVehicleTelemetry_ConnectionTimeout() {

    }

    void getVehicleTelemetry_InvalidJsonResponse() {

    }

    private String loadJsonFromFile(String fileName) throws IOException {
        Resource resource = new ClassPathResource(fileName);

        String jsonFileContent = Files.readString(resource.getFile().toPath());
        return jsonFileContent;

    }

}