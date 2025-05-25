package com.example.vehiclelogprocessing.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.vehiclelogprocessing.dtos.FootballApiResponseDto;
import com.example.vehiclelogprocessing.dtos.FootballMatchInfoDto;
import com.example.vehiclelogprocessing.exception.ExternalServiceException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class FootballService {
    private static final String API_URL = "https://jsonmock.hackerrank.com/api";
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;

    @Autowired
    public FootballService(@Qualifier("footballObjectMapper") ObjectMapper objectMapper,
            @Qualifier("footballHttpClient") HttpClient httpClient) {
        this.objectMapper = objectMapper;
        this.httpClient = httpClient;
    }

    private enum EventType {
        HOME_TEAM,
        AWAY_TEAM
    }

    public int getTotalGoalsByTeam(String teamName, int year) {
        try {
            return getGoalsByTeam(EventType.HOME_TEAM, teamName, year)
                    + getGoalsByTeam(EventType.AWAY_TEAM, teamName, year);
        } catch (ExternalServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -1;
        }
    }

    private int getGoalsByTeam(EventType eventType, String teamName, int year) throws ExternalServiceException {
        int totalPages = 1;
        int pageToFetch = 1;
        int goals = 0;
        while (pageToFetch <= totalPages) {
            URI uri = getUri(eventType, teamName, year, pageToFetch);
            FootballApiResponseDto footballApiResponseDto = getFootballApiResponseDto(uri);
            totalPages = footballApiResponseDto.getTotalPages();
            pageToFetch++;
            goals += footballApiResponseDto.getData()
                    .stream()
                    .map(footballMatchInfoDto -> getGoalsByeventType(eventType, teamName, footballMatchInfoDto))
                    .reduce(0, Integer::sum);
        }
        return goals;
    }

    private int getGoalsByeventType(EventType eventType, String teamName,
            FootballMatchInfoDto footballMatchInfoDto) {
        if (eventType == EventType.HOME_TEAM)
            return Integer.parseInt(footballMatchInfoDto.getTeam1Goals());
        else
            return Integer.parseInt(footballMatchInfoDto.getTeam2Goals());
    }

    private FootballApiResponseDto getFootballApiResponseDto(URI uri) throws ExternalServiceException {
        HttpRequest request = HttpRequest.newBuilder(uri)
                .GET().header("Accept", "application/json")
                .build();
        HttpResponse<String> response = null;

        try {
            response = httpClient.send(request, BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new ExternalServiceException("something went wrong", e);
        }
        int statusCode = response.statusCode();
        if (statusCode < 200 || statusCode >= 300)
            throw new ExternalServiceException("External service failed with status code " + statusCode);

        try {
            return objectMapper.readValue(response.body(), FootballApiResponseDto.class);
        } catch (JsonProcessingException e) {
            throw new ExternalServiceException("parsing error", e);
        }

    }

    private URI getUri(EventType eventType, String teamName, int year, int pageToFetch) {

        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(API_URL + "/football_matches")
                .queryParam("page", pageToFetch)
                .queryParam("year", year);
        if (eventType == EventType.HOME_TEAM)
            uriComponentsBuilder.queryParam("team1", teamName);
        else
            uriComponentsBuilder.queryParam("team2", teamName);
        return uriComponentsBuilder.build().toUri();
    }

}
