package com.kilic.ismyteammatetoxic.service;

import java.io.IOException;
import java.util.List;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kilic.ismyteammatetoxic.SecretFile;
import com.kilic.ismyteammatetoxic.api.dto.GetGameListItemDTO;
import com.kilic.ismyteammatetoxic.api.dto.GetRelevantPlayerInfoDTO;

import no.stelar7.api.r4j.basic.cache.impl.FileSystemCacheProvider;
import no.stelar7.api.r4j.basic.constants.api.regions.LeagueShard;
import no.stelar7.api.r4j.impl.R4J;
import no.stelar7.api.r4j.impl.lol.builders.championmastery.ChampionMasteryBuilder;
import no.stelar7.api.r4j.impl.lol.builders.matchv5.match.MatchBuilder;
import no.stelar7.api.r4j.impl.lol.builders.matchv5.match.TimelineBuilder;
import no.stelar7.api.r4j.pojo.lol.match.v5.LOLMatch;
import no.stelar7.api.r4j.pojo.lol.match.v5.LOLTimeline;
import no.stelar7.api.r4j.pojo.lol.match.v5.MatchParticipant;
import no.stelar7.api.r4j.pojo.lol.summoner.Summoner;

@Service
public class LolGameServiceImplementation implements LolGameService {

    final R4J r4J = new R4J(SecretFile.CREDS);
    Logger logger = LoggerFactory.getLogger(LolGameServiceImplementation.class);
    static Supplier<FileSystemCacheProvider> fileCache = () -> new FileSystemCacheProvider(50);

    @Autowired
    ToxicityCalculationServiceImplementation toxicityCalculationService;

    @Override
    public List<String> getMatchHistory(String accountId, String userRegion) {
        Summoner sum = Summoner.byAccountId(LeagueShard.valueOf(userRegion), accountId);
        List<String> matches = sum
                .getLeagueGames()
                .withCount(20)
                .get();

        return matches;
    }

    @Override
    public GetGameListItemDTO getMatch(String matchId, String userRegion) {
        MatchBuilder mb = new MatchBuilder(LeagueShard.valueOf(userRegion));

        mb = mb.withId(matchId);

        LOLMatch match = mb.getMatch();
        return GetGameListItemDTO.from(match);
    }

    @Override
    public GetRelevantPlayerInfoDTO getRelevantPlayerInfo(String accountId, String matchId, String userRegion)
            throws IOException {

        Summoner sum = Summoner.byAccountId(LeagueShard.valueOf(userRegion), accountId);

        TimelineBuilder tb = new TimelineBuilder(LeagueShard.valueOf(userRegion));
        MatchBuilder mb = new MatchBuilder(LeagueShard.valueOf(userRegion));
        ChampionMasteryBuilder cmb = new ChampionMasteryBuilder().withPlatform(LeagueShard.valueOf((userRegion))).withSummonerId(sum.getSummonerId());

        tb = tb.withId(matchId);
        mb = mb.withId(matchId);
        LOLMatch match = mb.getMatch();
        LOLTimeline timeline = tb.getTimeline();

        var wrapper = new Object() {
            MatchParticipant matchParticipant;
        };

        for (MatchParticipant participant : match.getParticipants()) {
            if (participant.getPuuid().equals(sum.getPUUID())) {
                wrapper.matchParticipant = participant;
                break;
            }
        }

        // Match related metadata
        if (wrapper.matchParticipant != null) {
            int champPoints =  cmb.withChampionId(wrapper.matchParticipant.getChampionId()).getChampionMastery().getChampionPoints();
            GetRelevantPlayerInfoDTO relevantPlayerInfoDTO = GetRelevantPlayerInfoDTO.from(
                    wrapper.matchParticipant.getChampionName(),
                    champPoints,
                    wrapper.matchParticipant.getKills(),
                    wrapper.matchParticipant.getDeaths(),
                    wrapper.matchParticipant.getAssists(),
                    wrapper.matchParticipant.getLane(),
                    wrapper.matchParticipant.getTeam(),
                    wrapper.matchParticipant.didWin(),
                    match.getQueue(),
                    toxicityCalculationService.calculateToxicityLevel(match, timeline,
                            wrapper.matchParticipant.getKills(),
                            wrapper.matchParticipant.getDeaths(),
                            wrapper.matchParticipant.getAssists(),
                            wrapper.matchParticipant.didWin(),
                            wrapper.matchParticipant.getLane(),
                            sum,
                            wrapper.matchParticipant));

            return relevantPlayerInfoDTO;
        }
        return null;
    }
}
