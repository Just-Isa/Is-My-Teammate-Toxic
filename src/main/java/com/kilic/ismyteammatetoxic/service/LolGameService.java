package com.kilic.ismyteammatetoxic.service;

import java.io.IOException;
import java.util.List;

import com.kilic.ismyteammatetoxic.api.dto.GetGameListItemDTO;
import com.kilic.ismyteammatetoxic.api.dto.GetRelevantPlayerInfoDTO;

public interface LolGameService {
    public List<String> getMatchHistory(String summonerName, String userRegion);

    public GetGameListItemDTO getMatch(String gameId, String userRegion);

    public GetRelevantPlayerInfoDTO getRelevantPlayerInfo(String summonerName, String gameId, String userRegion)
            throws IOException;
}
