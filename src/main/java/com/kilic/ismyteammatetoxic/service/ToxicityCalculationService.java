package com.kilic.ismyteammatetoxic.service;

import com.kilic.ismyteammatetoxic.api.dto.GetRelevantPlayerInfoDTO;
import com.kilic.ismyteammatetoxic.api.dto.GetToxicityDTO;

import no.stelar7.api.r4j.pojo.lol.match.v5.LOLMatch;
import no.stelar7.api.r4j.pojo.lol.match.v5.LOLTimeline;
import no.stelar7.api.r4j.pojo.lol.match.v5.MatchParticipant;
import no.stelar7.api.r4j.pojo.lol.summoner.Summoner;

public interface ToxicityCalculationService {
    public GetToxicityDTO claculateToxicityLevel(LOLMatch match, LOLTimeline timeline,
            GetRelevantPlayerInfoDTO relevantPlayerInfoDTO, Summoner summoner, MatchParticipant participant);
}
