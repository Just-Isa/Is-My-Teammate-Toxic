package com.kilic.ismyteammatetoxic.service;

import java.util.List;

import com.kilic.ismyteammatetoxic.api.dto.GetGameListItemDTO;
import com.merakianalytics.orianna.types.core.match.MatchHistory;

public interface LolGameService {
    public List<String> getMatchHistory(String summonerName, String userRegion);

    public GetGameListItemDTO getMatch(String gameId, String userRegion);
}
