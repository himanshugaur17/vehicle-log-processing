package com.example.vehiclelogprocessing.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.vehiclelogprocessing.entity.VehicleEntity;
import com.example.vehiclelogprocessing.entity.VehicleTelemetryEntity;
import com.example.vehiclelogprocessing.repository.VehicleRepository;
import com.example.vehiclelogprocessing.repository.VehicleTelemetryRepository;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class VehicleLogProcessingControllerTest {
    private final MockMvc mockMvcClient;
    private final VehicleTelemetryRepository vehicleTelemetryRepository;
    private final VehicleRepository vehicleRepository;

    @Autowired
    public VehicleLogProcessingControllerTest(MockMvc mockMvcClient,
            VehicleTelemetryRepository vehicleTelemetryRepository,
            VehicleRepository vehicleRepository) {
        this.mockMvcClient = mockMvcClient;
        this.vehicleTelemetryRepository = vehicleTelemetryRepository;
        this.vehicleRepository = vehicleRepository;
    }

    private static MockWebServer externalApiServer;
    private static final String MOCK_FILE_PATH = "data.json";

    @BeforeAll
    static void setUp() throws IOException {
        externalApiServer = new MockWebServer();
        externalApiServer.start();
    }

    @BeforeEach
    public void populateVehicle() {
        VehicleEntity vehicleEntity1 = new VehicleEntity(12345L, "Himanshu");
        VehicleEntity vehicleEntity2 = new VehicleEntity(67890L, "Gaur");
        vehicleRepository.saveAllAndFlush(List.of(vehicleEntity1, vehicleEntity2));
    }

    @AfterEach
    public void cleanUp() {
        vehicleRepository.deleteAll();
        vehicleTelemetryRepository.deleteAll();
    }

    @AfterAll
    static void tearDown() throws IOException {
        externalApiServer.shutdown();
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry propertyRegistry) {
        String mockApiUrl = externalApiServer.url("/").toString();
        propertyRegistry.add("external.api.vehicle.telemetry.url", () -> mockApiUrl);
    }

    @Test
    public void shouldFetchLogsAndPersistToDatabase() throws Exception {
        String fakeApiResponse = loadJson(MOCK_FILE_PATH);
        Long vehicleId = 1234L;
        externalApiServer
                .enqueue(new MockResponse().setBody(fakeApiResponse).addHeader("Content-Type", "application/json")
                        .setResponseCode(200));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/api/v1/vehicles/{vehicleId}/logs",
                vehicleId);
        mockMvcClient.perform(request.contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.ingested_telemetry_ids").isArray());

        List<VehicleTelemetryEntity> vehicleTelemetryEntities = vehicleTelemetryRepository
                .findAll(Sort.by(Sort.Direction.ASC, "vehicle.id"));
        Assertions.assertEquals(4, vehicleTelemetryEntities.size());

        VehicleTelemetryEntity vehicleTelemetryEntity1 = vehicleTelemetryEntities.get(0);
        VehicleTelemetryEntity vehicleTelemetryEntity3 = vehicleTelemetryEntities.get(2);
        Assertions.assertEquals(12345L, vehicleTelemetryEntity1.getVehicle().getId());
        Assertions.assertEquals(vehicleTelemetryEntity1.getOdometerKm().compareTo(BigDecimal.valueOf(50100)), 0,
                "Odometer value is not correct");
        Assertions.assertEquals(67890L, vehicleTelemetryEntity3.getVehicle().getId());
    }

    private String loadJson(String path) throws IOException {
        try {
            Resource resource = new ClassPathResource(path);
            return Files.readString(resource.getFile().toPath());
        } catch (Exception e) {
            throw new RuntimeException("Failed to load mock data", e);
        }

    }
}
