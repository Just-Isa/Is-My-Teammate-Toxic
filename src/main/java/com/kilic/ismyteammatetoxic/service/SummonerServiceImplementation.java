package com.kilic.ismyteammatetoxic.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.kilic.ismyteammatetoxic.api.dto.GetSummonerDTO;
import com.merakianalytics.orianna.types.common.Region;
import com.merakianalytics.orianna.types.core.summoner.Summoner;

@Service
public class SummonerServiceImplementation implements SummonerService {

    Logger logger = LoggerFactory.getLogger(SummonerServiceImplementation.class);

    public GetSummonerDTO getUser(String summonerName, String regionTag) {
        Region userRegion = null;
        if (regionTag != null) {
            for (Region r : Region.values()) {
                if (r.getTag().equalsIgnoreCase(regionTag)) {
                    userRegion = r;
                }
            }
        }
        if (userRegion != null) {
            Summoner sum = Summoner.named(summonerName).withRegion(Region.valueOf(userRegion.name())).get();
            if (sum != null) {
                return GetSummonerDTO.from(sum);
            }
        }
        return null;
    }
}
