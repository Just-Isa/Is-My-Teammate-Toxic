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

import com.kilic.ismyteammatetoxic.api.dto.GetSummonerDTO;
import com.kilic.ismyteammatetoxic.service.SummonerServiceImplementation;

@RestController
@RequestMapping("api")
public class SummonerRestController {

    @Autowired
    SummonerServiceImplementation summService;

    Logger logger = LoggerFactory.getLogger(SummonerRestController.class);

    @GetMapping("/user/{riotId}")
    public @ResponseBody GetSummonerDTO getUserHistory(
            @PathVariable String riotId,
            @RequestParam String region) {
        return summService.getUser(riotId, region);
    }
}
