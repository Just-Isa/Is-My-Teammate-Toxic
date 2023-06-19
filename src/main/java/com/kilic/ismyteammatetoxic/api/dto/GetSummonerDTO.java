package com.kilic.ismyteammatetoxic.api.dto;

import com.merakianalytics.orianna.types.core.summoner.Summoner;

public record GetSummonerDTO(
        String accountId,
        String id,
        int level,
        String name,
        String platform,
        int profileIconId,
        String puuid) {
    public static GetSummonerDTO from(Summoner s) {
        return new GetSummonerDTO(s.getAccountId(), s.getId(), s.getLevel(), s.getName(), s.getPlatform().name(),
                s.getProfileIcon().getId(),
                s.getPuuid());
    }
}
