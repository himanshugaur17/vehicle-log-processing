package com.example.vehiclelogprocessing.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.vehiclelogprocessing.service.FootballService;

@RestController
@RequestMapping("/api/v1/football")
public class FootBallController {

    private final FootballService footballService;

    public FootBallController(FootballService footballService) {
        this.footballService = footballService;
    }

    @GetMapping
    public ResponseEntity<Integer> getTotalGoalsByTeam(@RequestParam String teamName, @RequestParam int year) {
        return ResponseEntity.ok(footballService.getTotalGoalsByTeam(teamName, year));
    }

}
