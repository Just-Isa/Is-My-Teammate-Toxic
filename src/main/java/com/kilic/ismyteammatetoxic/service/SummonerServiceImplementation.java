package com.kilic.ismyteammatetoxic.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.kilic.ismyteammatetoxic.api.dto.GetSummonerDTO;

import no.stelar7.api.r4j.basic.cache.impl.FileSystemCacheProvider;
import no.stelar7.api.r4j.basic.constants.api.regions.LeagueShard;
import no.stelar7.api.r4j.impl.shared.AccountAPI;
import no.stelar7.api.r4j.pojo.lol.summoner.Summoner;
import no.stelar7.api.r4j.pojo.shared.RiotAccount;

@Service
public class SummonerServiceImplementation implements SummonerService {

    Logger logger = LoggerFactory.getLogger(SummonerServiceImplementation.class);
    FileSystemCacheProvider cache = new FileSystemCacheProvider();

    public GetSummonerDTO getUser(String riotId, String regionTag) {
        LeagueShard userRegion = null;
        if (regionTag != null) {
            for (LeagueShard r : LeagueShard.values()) {
                if (r.getValue().equalsIgnoreCase(regionTag)) {
                    userRegion = r;
                    break;
                }
            }
        }

        String name = riotId.split("-")[0];
        String tag = riotId.split("-")[1];

        RiotAccount acc = AccountAPI.getInstance().getAccountByTag(userRegion.toRegionShard(), name, tag);

        if (userRegion != null) {
            Summoner sum = Summoner.byPUUID(userRegion, acc.getPUUID());
            if (sum != null) {
                return GetSummonerDTO.from(sum, name, tag, userRegion);
            }
        }
        return null;
    }
}
