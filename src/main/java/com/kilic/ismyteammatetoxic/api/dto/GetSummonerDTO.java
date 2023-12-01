package com.kilic.ismyteammatetoxic.api.dto;

import no.stelar7.api.r4j.basic.constants.api.regions.LeagueShard;
import no.stelar7.api.r4j.pojo.lol.summoner.Summoner;

public record GetSummonerDTO(
        String accountId,
        int level,
        String name,
        String tag,
        String platform,
        int profileIconId,
        String puuid,
        LeagueShard leagueShard) {
    public static GetSummonerDTO from(Summoner s, String name, String tag, LeagueShard leagueShard) {
        return new GetSummonerDTO(s.getAccountId(), s.getSummonerLevel(), name, tag, s.getPlatform().name(),
                s.getProfileIconId(), s.getPUUID(), leagueShard);
    }
}
