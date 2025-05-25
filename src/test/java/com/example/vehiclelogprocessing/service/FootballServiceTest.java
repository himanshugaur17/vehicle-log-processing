package com.example.vehiclelogprocessing.service;

import static org.mockito.Mockito.times;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.vehiclelogprocessing.dtos.FootballApiResponseDto;
import com.example.vehiclelogprocessing.dtos.FootballMatchInfoDto;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class FootballServiceTest {
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private HttpClient httpClient;
    @Mock
    private HttpResponse<String> mockHomeHttpResponse;
    @Mock
    private HttpResponse<String> mockAwayHttpResponse;

    private FootballService footballService;
    private static final String API_URL = "https://jsonmock.hackerrank.com/api";

    @BeforeEach
    public void setUp() {
        footballService = new FootballService(objectMapper, httpClient);
    }

    @Test
    public void getTotalGoalsByTeam_shouldReturnCorrectTotalGoals_whenSinglePage() throws Exception {
        String teamName = "Barcelona";
        int year = 2020;
        FootballMatchInfoDto team1MatchInfoDto = new FootballMatchInfoDto(teamName, "Opponent 2", "3", "1");
        FootballApiResponseDto team1FootballApiResponseDto1 = new FootballApiResponseDto(1, 20,
                List.of(team1MatchInfoDto), 1);
        String team1Json = "{\"data\":[{\"team1goals\":\"3\"}],\"total_pages\":1}";

        FootballMatchInfoDto team2MatchInfoDto = new FootballMatchInfoDto("Opponent 2", teamName, "1", "2");
        FootballApiResponseDto team2FootballApiResponseDto1 = new FootballApiResponseDto(1, 20,
                List.of(team2MatchInfoDto), 1);
        String team2Json = "{\"data\":[{\"team2goals\":\"2\"}],\"total_pages\":1}";
        URI homeUri = buildUri("team1", teamName, year, 1);
        URI awayUri = buildUri("team2", teamName, year, 1);
        setupMockApiReponse(homeUri, team1Json, mockHomeHttpResponse);
        setupMockApiReponse(awayUri, team2Json, mockAwayHttpResponse);
        Mockito.when(objectMapper.readValue(Mockito.eq(team1Json), Mockito.eq(FootballApiResponseDto.class)))
                .thenReturn(team1FootballApiResponseDto1);
        Mockito.when(objectMapper.readValue(Mockito.eq(team2Json), Mockito.eq(FootballApiResponseDto.class)))
                .thenReturn(team2FootballApiResponseDto1);

        int totalGoals = footballService.getTotalGoalsByTeam(teamName, year);

        Assertions.assertEquals(5, totalGoals);
        Mockito.verify(httpClient, times(2)).send(Mockito.any(java.net.http.HttpRequest.class),
                Mockito.eq(BodyHandlers.ofString()));
    }

    private void setupMockApiReponse(URI uri, String jsonBody, HttpResponse<String> httpResponse)
            throws IOException, InterruptedException {

        Mockito.when(
                httpClient.send(Mockito.argThat(new HttpRequestUriMatcher(uri)), Mockito.eq(BodyHandlers.ofString())))
                .thenReturn(httpResponse);
        Mockito.when(httpResponse.body()).thenReturn(jsonBody);
        Mockito.when(httpResponse.statusCode()).thenReturn(200);
    }

    private URI buildUri(String teamParamValue, String teamName, int year, int page) {
        return UriComponentsBuilder.fromUriString(API_URL + "/football_matches")
                .queryParam("page", page)
                .queryParam("year", year)
                .queryParam(teamParamValue, teamName)
                .build().toUri();
    }

    static class HttpRequestUriMatcher implements ArgumentMatcher<HttpRequest> {
        private final URI expectedUri;

        public HttpRequestUriMatcher(URI expectedUri) {
            this.expectedUri = expectedUri;
        }

        @Override
        public boolean matches(HttpRequest actualRequest) {
            if (actualRequest == null)
                return false;
            return expectedUri.equals(actualRequest.uri());
        }

    }

}
