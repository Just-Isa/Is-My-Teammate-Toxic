package com.kilic.ismyteammatetoxic.service;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.gson.stream.JsonWriter;
import com.kilic.ismyteammatetoxic.SecretFile;
import com.kilic.ismyteammatetoxic.api.dto.GetGameListItemDTO;
import com.kilic.ismyteammatetoxic.api.dto.GetRelevantPlayerInfoDTO;
import com.merakianalytics.orianna.types.core.match.Event;
import com.merakianalytics.orianna.types.core.match.Match.Participant;
import com.google.gson.JsonParser;

import no.stelar7.api.r4j.basic.cache.impl.FileSystemCacheProvider;
import no.stelar7.api.r4j.basic.constants.api.regions.LeagueShard;
import no.stelar7.api.r4j.basic.constants.types.lol.EventType;
import no.stelar7.api.r4j.basic.constants.types.lol.LaneType;
import no.stelar7.api.r4j.basic.constants.types.lol.SpellSlotType;
import no.stelar7.api.r4j.basic.utils.Utils;
import no.stelar7.api.r4j.impl.R4J;
import no.stelar7.api.r4j.impl.lol.builders.matchv5.match.MatchBuilder;
import no.stelar7.api.r4j.impl.lol.builders.matchv5.match.TimelineBuilder;
import no.stelar7.api.r4j.pojo.lol.match.v5.LOLMatch;
import no.stelar7.api.r4j.pojo.lol.match.v5.LOLTimeline;
import no.stelar7.api.r4j.pojo.lol.match.v5.MatchParticipant;
import no.stelar7.api.r4j.pojo.lol.match.v5.TimelineDamageData;
import no.stelar7.api.r4j.pojo.lol.match.v5.TimelineParticipantFrame;
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

    @Override
    public GetRelevantPlayerInfoDTO getRelevantPlayerInfo(String summonerName, String matchId, String userRegion)
            throws IOException {

        Summoner sum = Summoner.byName(LeagueShard.valueOf(userRegion), summonerName);

        TimelineBuilder tb = new TimelineBuilder(LeagueShard.valueOf(userRegion));
        MatchBuilder mb = new MatchBuilder(LeagueShard.valueOf(userRegion));

        tb = tb.withId(matchId);
        mb = mb.withId(matchId);

        LOLMatch match = mb.getMatch();
        LOLTimeline timeline = tb.getTimeline();
        List<TimelineDamageData> wierdEntries = new ArrayList<>();

        var wrapper = new Object() {
            MatchParticipant matchParticipant;
            int kills = -1;
            int deaths = -1;
            int assists = -1;
            String championName = "";
            LaneType lane;
            boolean win;
        };

        for (MatchParticipant participant : match.getParticipants()) {
            if (participant.getPuuid().equals(sum.getPUUID())) {
                wrapper.matchParticipant = participant;
                break;
            }
        }

        // Match related metadata
        if (wrapper.matchParticipant != null) {
            wrapper.championName = wrapper.matchParticipant.getChampionName();
            wrapper.deaths = wrapper.matchParticipant.getDeaths();
            wrapper.kills = wrapper.matchParticipant.getKills();
            wrapper.lane = wrapper.matchParticipant.getLane();
            wrapper.win = wrapper.matchParticipant.didWin();
            wrapper.assists = wrapper.matchParticipant.getAssists();
        }
        return GetRelevantPlayerInfoDTO.from(
                wrapper.championName, 0,
                wrapper.kills,
                wrapper.deaths,
                wrapper.assists,
                wrapper.lane,
                wrapper.win);

        /*
         * if (wrapper.participantId != -1) {
         * logger.info("participantID = {}", wrapper.participantId);
         * }
         * 
         * match.getParticipants().forEach(participant -> {
         * 
         * timeline.getFrames().forEach(frame -> {
         * frame.getEvents().forEach(event -> {
         * if (event.getType() == EventType.ITEM_PURCHASED
         * && event.getParticipantId() == wrapper.participantId) {
         * }
         * });
         * });
         * });
         */

    }

}
