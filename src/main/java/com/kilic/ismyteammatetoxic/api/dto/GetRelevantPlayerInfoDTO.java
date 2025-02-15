package com.kilic.ismyteammatetoxic.api.dto;

import no.stelar7.api.r4j.basic.constants.types.lol.GameQueueType;
import no.stelar7.api.r4j.basic.constants.types.lol.LaneType;
import no.stelar7.api.r4j.basic.constants.types.lol.TeamType;

public record GetRelevantPlayerInfoDTO(
        String champName,
        int kills,
        int deaths,
        int assists,
        LaneType lane,
        TeamType team,
        boolean win,
        GameQueueType gameQueueType,
        GetToxicityDTO toxicityDTO) {
    public static GetRelevantPlayerInfoDTO from(
            String champString,
            int kills,
            int deaths,
            int assists,
            LaneType lane,
            TeamType team,
            boolean win,
            GameQueueType gameQueueType,
            GetToxicityDTO toxicityDTO) {
        return new GetRelevantPlayerInfoDTO(champString, kills, deaths, assists, lane, team, win,
                gameQueueType,
                toxicityDTO);
    }

}
