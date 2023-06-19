package com.kilic.ismyteammatetoxic.service;

import com.kilic.ismyteammatetoxic.api.dto.GetSummonerDTO;
import com.merakianalytics.orianna.types.common.Region;

public interface SummonerService {
    public GetSummonerDTO getUser(String summonerName, String regionTag);
}
