package com.kilic.ismyteammatetoxic.service;

import java.util.List;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.kilic.ismyteammatetoxic.SecretFile;
import com.kilic.ismyteammatetoxic.api.dto.GetGameListItemDTO;

import no.stelar7.api.r4j.basic.cache.impl.FileSystemCacheProvider;
import no.stelar7.api.r4j.basic.constants.api.regions.LeagueShard;
import no.stelar7.api.r4j.impl.R4J;
import no.stelar7.api.r4j.impl.lol.builders.matchv5.match.MatchBuilder;
import no.stelar7.api.r4j.pojo.lol.match.v5.LOLMatch;
import no.stelar7.api.r4j.pojo.lol.summoner.Summoner;

@Service
public class LolGameServiceImplementation implements LolGameService {

    final R4J r4J = new R4J(SecretFile.CREDS);
    Logger logger = LoggerFactory.getLogger(LolGameServiceImplementation.class);
    static Supplier<FileSystemCacheProvider> fileCache = () -> new FileSystemCacheProvider(50);

    @Override
    public List<String> getMatchHistory(String summonerName, String userRegion) {
        Summoner sum = Summoner.byName(LeagueShard.valueOf(userRegion), summonerName);
        List<String> matches = sum.getLeagueGames().get();

        return matches;
    }

    @Override
    public GetGameListItemDTO getMatch(String matchId, String userRegion) {
        MatchBuilder mb = new MatchBuilder(LeagueShard.valueOf(userRegion));

        mb = mb.withId(matchId);

        LOLMatch match = mb.getMatch();
        return GetGameListItemDTO.from(match);
    }

}
