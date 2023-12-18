package com.kilic.ismyteammatetoxic.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.kilic.ismyteammatetoxic.api.dto.GetGameListItemDTO;
import com.kilic.ismyteammatetoxic.api.dto.GetRelevantPlayerInfoDTO;

import no.stelar7.api.r4j.pojo.lol.championmastery.ChampionMastery;

public interface LolGameService {
    public List<String> getMatchHistory(String accountId, String userRegion);

    public List<ChampionMastery> getPlayerMastery(String accountId, String userRegion);

    public GetGameListItemDTO getMatch(String gameId, String userRegion);

    public GetRelevantPlayerInfoDTO getRelevantPlayerInfo(String accountId, String gameId, String userRegion)
            throws IOException;

    public Map<Integer, String> getChamps();
}
