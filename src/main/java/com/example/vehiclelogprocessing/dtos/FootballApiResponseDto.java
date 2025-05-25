package com.example.vehiclelogprocessing.dtos;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FootballApiResponseDto {
    @JsonProperty("page")
    private final int page;
    @JsonProperty("per_page")
    private final int perPage;
    @JsonProperty("data")
    private final List<FootballMatchInfoDto> data;
    @JsonProperty("total_pages")
    private final int totalPages;

    public int getPage() {
        return page;
    }

    public int getPerPage() {
        return perPage;
    }

    public List<FootballMatchInfoDto> getData() {
        return Collections.unmodifiableList(data);
    }

    public int getTotalPages() {
        return totalPages;
    }

    public FootballApiResponseDto(@JsonProperty("page") int page, @JsonProperty("per_page") int perPage,
            @JsonProperty("data") List<FootballMatchInfoDto> data,
            @JsonProperty("total_pages") int totalPages) {
        this.page = page;
        this.perPage = perPage;
        this.data = data;
        this.totalPages = totalPages;
    }

}
