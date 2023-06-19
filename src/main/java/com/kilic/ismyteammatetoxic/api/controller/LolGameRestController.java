package com.kilic.ismyteammatetoxic.api.controller;

import java.io.Console;
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

import com.kilic.ismyteammatetoxic.api.dto.GetGameListItemDTO;
import com.kilic.ismyteammatetoxic.config.YAMLConfig;
import com.kilic.ismyteammatetoxic.service.LolGameService;

@RestController
@RequestMapping("api")
public class LolGameRestController {

        @Autowired
        private YAMLConfig config;

        @Autowired
        private LolGameService lolGameService;

        Logger logger = LoggerFactory.getLogger(LolGameRestController.class);

        @GetMapping("/lol/matchhistory/{summonerName}")
        public @ResponseBody List<String> getLolMatcheHistory(
                        @PathVariable String summonerName,
                        @RequestParam String region) {
                return lolGameService.getMatchHistory(summonerName, region);
        }

        @GetMapping("/lol/game/{gameID}")
        public @ResponseBody GetGameListItemDTO getLolMatch(
                        @PathVariable String gameID,
                        @RequestParam String region) {
                return lolGameService.getMatch(gameID, region);
        }

        @GetMapping("/lol/game/{gameID}/timeline")
        public @ResponseBody void getLolMatchTimeline(
                        @PathVariable String gameID,
                        @RequestParam String region) {

        }
}
