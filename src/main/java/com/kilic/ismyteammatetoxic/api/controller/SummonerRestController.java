package com.kilic.ismyteammatetoxic.api.controller;

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
import com.kilic.ismyteammatetoxic.SecretFile;
import com.kilic.ismyteammatetoxic.api.dto.GetSummonerDTO;
import com.kilic.ismyteammatetoxic.service.SummonerServiceImplementation;

@RestController
@RequestMapping("api")
public class SummonerRestController {

    @Autowired
    SummonerServiceImplementation summService;

    Logger logger = LoggerFactory.getLogger(SummonerRestController.class);

    @GetMapping("/user/{summonerName}")
    public @ResponseBody GetSummonerDTO getUserHistory(
            @PathVariable String summonerName,
            @RequestParam String region) {

        Orianna.setRiotAPIKey(SecretFile.LEAGUE_KEY);
        return summService.getUser(summonerName, region);
    }
}
