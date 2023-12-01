package com.kilic.ismyteammatetoxic.service;

import com.kilic.ismyteammatetoxic.api.dto.GetSummonerDTO;

public interface SummonerService {
    public GetSummonerDTO getUser(String riotId, String regionTag);
}
