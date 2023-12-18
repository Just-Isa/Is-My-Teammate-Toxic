package com.kilic.ismyteammatetoxic.api.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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
import com.kilic.ismyteammatetoxic.api.dto.GetRelevantPlayerInfoDTO;
import com.kilic.ismyteammatetoxic.service.LolGameService;

import no.stelar7.api.r4j.pojo.lol.championmastery.ChampionMastery;

@RestController
@RequestMapping("api")
public class LolGameRestController {

        @Autowired
        private LolGameService lolGameService;

        Logger logger = LoggerFactory.getLogger(LolGameRestController.class);

        @GetMapping("/lol/matchhistory/{accountId}")
        public @ResponseBody List<String> getLolMatchHistory(
                        @PathVariable String accountId,
                        @RequestParam String region) {
                return lolGameService.getMatchHistory(accountId, region);
        }

        @GetMapping("/lol/playerMastery/{accountId}")
        public @ResponseBody List<ChampionMastery> getLolPlayerMastery(
                        @PathVariable String accountId,
                        @RequestParam String region) {
                return lolGameService.getPlayerMastery(accountId, region);
        }

        @GetMapping("/lol/game/{gameID}")
        public @ResponseBody GetGameListItemDTO getLolMatch(
                        @PathVariable String gameID,
                        @RequestParam String region) {
                return lolGameService.getMatch(gameID, region);
        }

        @GetMapping("/lol/game/{gameID}/timeline")
        public @ResponseBody GetRelevantPlayerInfoDTO getRelevantPlayerInfo(
                        @PathVariable String gameID,
                        @RequestParam String accountId,
                        @RequestParam String region) throws IOException {
                return lolGameService.getRelevantPlayerInfo(accountId, gameID, region);
        }

        @GetMapping("/lol/champs")
        public @ResponseBody Map<Integer, String> getChamps() throws IOException {
                logger.info("{}", lolGameService.getChamps());                logger.info("{}", lolGameService.getChamps());

                return lolGameService.getChamps();
        }
}
