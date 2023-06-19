package com.kilic.ismyteammatetoxic.api.controller;

import java.io.Console;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.merakianalytics.orianna.Orianna;

import no.stelar7.api.r4j.basic.APICredentials;
import no.stelar7.api.r4j.impl.R4J;

import com.kilic.ismyteammatetoxic.api.dto.GetSummonerDTO;
import com.kilic.ismyteammatetoxic.config.YAMLConfig;
import com.kilic.ismyteammatetoxic.service.SummonerServiceImplementation;

@RestController
@RequestMapping("api")
public class SummonerRestController {

    @Autowired
    private YAMLConfig config;

    @Autowired
    SummonerServiceImplementation summService;

    Logger logger = LoggerFactory.getLogger(SummonerRestController.class);

    @GetMapping("/user/{summonerName}")
    public @ResponseBody GetSummonerDTO getUserHistory(
            @PathVariable String summonerName,
            @RequestParam String region) {

        R4J api = new R4J(new APICredentials(config.getApiKey()));
        Orianna.setRiotAPIKey(config.getApiKey());
        return summService.getUser(summonerName, region);
    }
}
