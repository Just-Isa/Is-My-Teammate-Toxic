package com.kilic.ismyteammatetoxic.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.metrics.buffering.StartupTimeline.TimelineEvent;
import org.springframework.stereotype.Service;

import com.kilic.ismyteammatetoxic.api.dto.GetRelevantPlayerInfoDTO;
import com.kilic.ismyteammatetoxic.api.dto.GetToxicityDTO;
import com.kilic.ismyteammatetoxic.domain.ToxicityValue;
import com.merakianalytics.orianna.types.core.staticdata.Champion;

import java.util.Map;

import no.stelar7.api.r4j.basic.constants.types.lol.EventType;
import no.stelar7.api.r4j.basic.constants.types.lol.GameQueueType;
import no.stelar7.api.r4j.impl.lol.builders.champion.ChampionBuilder;
import no.stelar7.api.r4j.impl.lol.raw.ChampionAPI;
import no.stelar7.api.r4j.pojo.lol.match.v5.LOLMatch;
import no.stelar7.api.r4j.pojo.lol.match.v5.LOLTimeline;
import no.stelar7.api.r4j.pojo.lol.match.v5.MatchParticipant;
import no.stelar7.api.r4j.pojo.lol.match.v5.TimelineFrame;
import no.stelar7.api.r4j.pojo.lol.match.v5.TimelineFrameEvent;
import no.stelar7.api.r4j.pojo.lol.match.v5.TimelineParticipantFrame;
import no.stelar7.api.r4j.pojo.lol.staticdata.champion.StaticChampionList;
import no.stelar7.api.r4j.pojo.lol.staticdata.item.Item;
import no.stelar7.api.r4j.pojo.lol.staticdata.item.ItemList;
import no.stelar7.api.r4j.pojo.lol.summoner.Summoner;

@Service
public class ToxicityCalculationServiceImplementation {

    Logger logger = LoggerFactory.getLogger(ToxicityCalculationServiceImplementation.class);
    private final String[] toxicNames = {
            "draven",
            "twitch",
            "xd",
            "reformed",
            "l9",
            "schizo",
            "report",
            "his ",
            "her ",
            "uwu",
            "owo",
            "PL",
            "TR",
            "FR"
    };

    public GetToxicityDTO claculateToxicityLevel(LOLMatch match, LOLTimeline timeline,
            GetRelevantPlayerInfoDTO relevantPlayerInfoDTO, Summoner sum, MatchParticipant participant) {

        List<ToxicityValue> toxicityValues = new ArrayList<>();
        List<TimelineFrame> pre10MinFrames = new ArrayList<>();
        List<TimelineFrame> post10pre2minBeforeEndFrames = new ArrayList<>();
        List<TimelineFrame> last2MinFrames = new ArrayList<>();
        // starts at 0, goes up from there
        float toxicity = 0;
        // deaths
        int deathspre10 = 0;
        int deathspost10Pre2Min = 0;
        int deathsPre2MinBeforeEnd = 0;
        // items
        Map<Integer, Integer> itemsBoughtpost10Pre2 = new HashMap<>();
        Map<Integer, Integer> itemsBoughtpre10 = new HashMap<>();
        Map<Integer, Integer> itemsBoughtPre2MinBeforeEnd = new HashMap<>();

        int amountSameItems = 0;
        int idSameItem = 0;

        List<Integer> participantItems = new ArrayList<>() {
            {
                add(participant.getItem0());
                add(participant.getItem1());
                add(participant.getItem2());
                add(participant.getItem3());
                add(participant.getItem4());
                add(participant.getItem5());
                add(participant.getItem6());
            }
        };

        for (Integer itemId : participantItems) {
            if (Collections.frequency(participantItems, itemId) > 2) {
                amountSameItems = Collections.frequency(participantItems, itemId);
                idSameItem = itemId;
            }
        }
        for (TimelineFrame frame : timeline.getFrames()) {
            TimelineParticipantFrame participantFrame = frame.getParticipantFrames()
                    .get(Integer.toString(participant.getParticipantId()));

            if (frame.getTimestamp() < 600000) {
                pre10MinFrames.add(frame);
            }
            if (frame.getTimestamp() <= match.getGameDurationAsDuration().toMillis() - 120000
                    && frame.getTimestamp() > 600000) {
                post10pre2minBeforeEndFrames.add(frame);
            }
            if (frame.getTimestamp() >= match.getGameDurationAsDuration().toMillis() - 120000) {
                last2MinFrames.add(frame);
            }
        }

        // first 15 min calc
        for (TimelineFrame frame : pre10MinFrames) {
            for (TimelineFrameEvent event : frame.getEvents()) {
                switch (event.getType()) {
                    case CHAMPION_KILL:
                        if (event.getVictimId() == participant.getParticipantId()) {
                            deathspre10++;
                        }
                        break;
                    case ITEM_PURCHASED:
                        if (itemsBoughtpre10.get(event.getItemId()) != null) {
                            itemsBoughtpre10.merge(event.getItemId(), 1, Integer::sum);

                            break;
                        }
                        itemsBoughtpre10.put(event.getItemId(), 1);
                    default:
                        break;
                }
            }
        }
        // inbetween calc
        for (TimelineFrame frame : post10pre2minBeforeEndFrames) {
            for (TimelineFrameEvent event : frame.getEvents()) {
                switch (event.getType()) {
                    case CHAMPION_KILL:
                        if (event.getVictimId() == participant.getParticipantId()) {
                            deathspost10Pre2Min++;
                        }
                        break;
                    case ITEM_PURCHASED:
                        if (itemsBoughtpost10Pre2.get(event.getItemId()) != null) {
                            itemsBoughtpost10Pre2.merge(event.getItemId(), 1, Integer::sum);

                            break;
                        }
                        itemsBoughtpost10Pre2.put(event.getItemId(), 1);
                        break;
                    default:
                        break;
                }
            }
        }
        // last 5 min calc
        for (TimelineFrame frame : last2MinFrames) {
            for (TimelineFrameEvent event : frame.getEvents()) {
                switch (event.getType()) {
                    case CHAMPION_KILL:
                        if (event.getVictimId() == participant.getParticipantId()) {
                            deathsPre2MinBeforeEnd++;
                        }
                        break;
                    case ITEM_PURCHASED:
                        if (itemsBoughtPre2MinBeforeEnd.get(event.getItemId()) != null) {
                            itemsBoughtPre2MinBeforeEnd.merge(event.getItemId(), 1, Integer::sum);
                            break;
                        }
                        itemsBoughtPre2MinBeforeEnd.put(event.getItemId(), 1);
                        break;
                    default:
                        break;
                }
            }
        }

        // check for item troll

        if (idSameItem != 0) {
            if (itemsBoughtpost10Pre2.get(idSameItem) != null) {
                if (itemsBoughtpost10Pre2.get(idSameItem) >= amountSameItems) {
                    toxicityValues.add(ToxicityValue.TROLLITEMS);
                }
            } else if (itemsBoughtpre10.get(idSameItem) != null) {
                if (itemsBoughtpre10.get(idSameItem) >= amountSameItems) {
                    toxicityValues.add(ToxicityValue.TROLLITEMS);
                }
            }
        }

        // check for inting
        if (match.getQueue() != GameQueueType.ARAM) {
            toxicityValues = interCheck(toxicityValues, relevantPlayerInfoDTO, match, deathspre10, deathspost10Pre2Min);
        }

        // Toxic Name check
        if (this.checkForToxicName(sum.getName())) {
            toxicityValues.add(ToxicityValue.TOXICNAME);
        }

        // Check if player went AFK or PICKED TROLL
        toxicityValues = this.afkOrTrollCheck(toxicityValues, relevantPlayerInfoDTO, match, participant);

        // ---------------------------------------------------------------------------------------------

        // Add up toxicity Value
        for (ToxicityValue value : toxicityValues) {
            toxicity += value.value;
        }

        logger.info("{}", GetToxicityDTO.from(toxicity, toxicityValues));

        return GetToxicityDTO.from(toxicity, toxicityValues);
    }

    private List<ToxicityValue> interCheck(List<ToxicityValue> values, GetRelevantPlayerInfoDTO relevantPlayerInfoDTO,
            LOLMatch match, int deathspre10, int deathspost10Pre2Min) {
        if (deathspre10 > 10) {
            values.add(ToxicityValue.EARLYINTER);
        }
        if (deathspost10Pre2Min > 20) {
            values.add(ToxicityValue.INTER);
        }
        if (relevantPlayerInfoDTO.deaths() >= 20
                && (relevantPlayerInfoDTO.kills() <= 5 && relevantPlayerInfoDTO.assists() <= 5)) {
            values.add(ToxicityValue.FEEDING);
        } else if (relevantPlayerInfoDTO.deaths() > 10
                && (relevantPlayerInfoDTO.kills() <= 3 && relevantPlayerInfoDTO.assists() <= 3)) {
            values.add(ToxicityValue.BADSCORE);
        }
        return values;
    }

    private List<ToxicityValue> afkOrTrollCheck(List<ToxicityValue> values,
            GetRelevantPlayerInfoDTO relevantPlayerInfoDTO, LOLMatch match, MatchParticipant participant) {
        switch (relevantPlayerInfoDTO.lane()) {
            case AFK:
                values.add(ToxicityValue.AFK);
                break;
            case NONE:
                break;
            case BOT:
                break;
            case INVALID:
                break;
            case JUNGLE:
                break;
            case MID:
                break;
            case TOP:
                break;
            case UTILITY:
                break;
            default:
                if (relevantPlayerInfoDTO.deaths() == 0 || match.getQueue() == GameQueueType.ARAM) {
                    break;
                }
                if (relevantPlayerInfoDTO.win()
                        && ((relevantPlayerInfoDTO.kills() + (relevantPlayerInfoDTO.assists() * 0.5))
                                / relevantPlayerInfoDTO.deaths()) > 0.8) {
                    values.add(ToxicityValue.TROLLBUTWIN);
                    break;
                }
                values.add(ToxicityValue.TROLL);
                break;
        }
        return values;
    }

    private boolean checkForToxicName(String summonerName) {
        return Arrays.stream(toxicNames).anyMatch(summonerName.toLowerCase()::contains);
    }
}
