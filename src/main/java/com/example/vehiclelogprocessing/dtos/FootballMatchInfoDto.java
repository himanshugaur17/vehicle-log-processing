package com.example.vehiclelogprocessing.dtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FootballMatchInfoDto {

    @JsonProperty("team1")
    private final String team1;
    @JsonProperty("team2")
    private final String team2;
    @JsonProperty("team1goals")
    private final String team1Goals;
    @JsonProperty("team2goals")
    private final String team2Goals;

    public String getTeam1() {
        return team1;
    }

    public String getTeam2() {
        return team2;
    }

    public String getTeam1Goals() {
        return team1Goals;
    }

    public String getTeam2Goals() {
        return team2Goals;
    }

    @JsonCreator
    public FootballMatchInfoDto(@JsonProperty("team1") String team1,
            @JsonProperty("team2") String team2,
            @JsonProperty("team1goals") String team1Goals,
            @JsonProperty("team2goals") String team2Goals) {
        this.team1 = team1;
        this.team1Goals = team1Goals;
        this.team2 = team2;
        this.team2Goals = team2Goals;

    }
}
