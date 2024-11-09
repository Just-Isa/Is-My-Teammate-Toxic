package com.kilic.ismyteammatetoxic.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.kilic.ismyteammatetoxic.api.dto.GetDeathPositionAndTimestampDTO;
import com.kilic.ismyteammatetoxic.api.dto.GetToxicityDTO;
import com.kilic.ismyteammatetoxic.domain.ToxicityValue;

import no.stelar7.api.r4j.basic.constants.types.lol.GameQueueType;
import no.stelar7.api.r4j.basic.constants.types.lol.LaneType;
import no.stelar7.api.r4j.pojo.lol.match.v5.LOLMatch;
import no.stelar7.api.r4j.pojo.lol.match.v5.LOLTimeline;
import no.stelar7.api.r4j.pojo.lol.match.v5.MatchParticipant;
import no.stelar7.api.r4j.pojo.lol.match.v5.TimelineFrame;
import no.stelar7.api.r4j.pojo.lol.match.v5.TimelineFrameEvent;
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
            "FR",
            "spacegliding",
            "testosterone"
    };

    public GetToxicityDTO calculateToxicityLevel(LOLMatch match, LOLTimeline timeline,
            int kills, int deaths, int assists, boolean win, LaneType lane, Summoner sum,
            MatchParticipant participant) {

        List<ToxicityValue> toxicityValues = new ArrayList<>();

        List<TimelineFrame> pre10MinFrames = new ArrayList<>();
        List<GetDeathPositionAndTimestampDTO> pre10MinDeathPositions = new ArrayList<>();

        List<TimelineFrame> post10pre2minBeforeEndFrames = new ArrayList<>();
        List<GetDeathPositionAndTimestampDTO> post10pre2minBeforeEndDeathPositions = new ArrayList<>();

        List<TimelineFrame> last2MinFrames = new ArrayList<>();
        List<GetDeathPositionAndTimestampDTO> last2MinDeathPositions = new ArrayList<>();

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

        List<Integer> participantItems = List.of(
                participant.getItem0(),
                participant.getItem1(),
                participant.getItem2(),
                participant.getItem3(),
                participant.getItem4(),
                participant.getItem5(),
                participant.getItem6()
        );

        for (TimelineFrame frame : timeline.getFrames()) {
            long timestamp = frame.getTimestamp();
            if (timestamp < 600000) {
                pre10MinFrames.add(frame);
            } else if (timestamp <= match.getGameDurationAsDuration().toMillis() - 120000) {
                post10pre2minBeforeEndFrames.add(frame);
            } else {
                last2MinFrames.add(frame);
            }

            for (TimelineFrameEvent event : frame.getEvents()) {
                switch (event.getType()) {
                    case CHAMPION_KILL:
                        if (event.getVictimId() == participant.getParticipantId()) {
                            if (timestamp < 600000) {
                                pre10MinDeathPositions.add(GetDeathPositionAndTimestampDTO.from(event.getPosition(), event.getTimestamp()));
                                deathspre10++;
                            } else if (timestamp <= match.getGameDurationAsDuration().toMillis() - 120000) {
                                post10pre2minBeforeEndDeathPositions.add(GetDeathPositionAndTimestampDTO.from(event.getPosition(), event.getTimestamp()));
                                deathspost10Pre2Min++;
                            } else {
                                last2MinDeathPositions.add(GetDeathPositionAndTimestampDTO.from(event.getPosition(), event.getTimestamp()));
                                deathsPre2MinBeforeEnd++;
                            }
                        }
                        break;
                    case ITEM_PURCHASED:
                        if (timestamp < 600000) {
                            itemsBoughtpre10.merge(event.getItemId(), 1, Integer::sum);
                        } else if (timestamp <= match.getGameDurationAsDuration().toMillis() - 120000) {
                            itemsBoughtpost10Pre2.merge(event.getItemId(), 1, Integer::sum);
                        } else {
                            itemsBoughtPre2MinBeforeEnd.merge(event.getItemId(), 1, Integer::sum);
                        }
                        break;
                    default:
                        break;
                }
            }
        }

        // check for item troll
        if (idSameItem != 0) {
            if (itemsBoughtpost10Pre2.getOrDefault(idSameItem, 0) >= amountSameItems
                    || itemsBoughtpre10.getOrDefault(idSameItem, 0) >= amountSameItems) {
                toxicityValues.add(ToxicityValue.TROLLITEMS);
            }
        }

        // check for inting
        if (match.getQueue() != GameQueueType.ARAM && match.getQueue() != GameQueueType.CHERRY) {
            toxicityValues = interCheck(toxicityValues, kills, deaths, assists, deathspre10, deathspost10Pre2Min);
        }

        // Toxic Name check
        /*
        if (checkForToxicName(sum.getName())) {
            toxicityValues.add(ToxicityValue.TOXICNAME);
        }
 */
        // Check if player went AFK or PICKED TROLL
        toxicityValues = afkOrTrollCheck(toxicityValues, deaths, kills, assists, win, lane, match, participant);

        // Add up toxicity Value
        for (ToxicityValue value : toxicityValues) {
            toxicity += value.value;
        }

        return GetToxicityDTO.from(toxicity, toxicityValues, pre10MinDeathPositions,
                post10pre2minBeforeEndDeathPositions, last2MinDeathPositions);
    }

    private List<ToxicityValue> interCheck(List<ToxicityValue> values, int kills, int deaths, int assists,
            int deathspre10, int deathspost10Pre2Min) {
        if (deathspre10 > 10) {
            values.add(ToxicityValue.EARLYINTER);
        }
        if (deathspost10Pre2Min > 20) {
            values.add(ToxicityValue.INTER);
        }
        if (deaths >= 20 && (kills <= 5 && assists <= 5)) {
            values.add(ToxicityValue.FEEDING);
        } else if (deaths > 10 && (kills <= 3 && assists <= 3)) {
            values.add(ToxicityValue.BADSCORE);
        }
        return values;
    }

    private List<ToxicityValue> afkOrTrollCheck(List<ToxicityValue> values,
            int deaths, int kills, int assists, boolean win, LaneType lane, LOLMatch match,
            MatchParticipant participant) {
        switch (lane) {
            case AFK:
                values.add(ToxicityValue.AFK);
                break;
            case UTILITY:
                //ADD CHECK FOR KSING SUPPORT HERE
                break;
            default:
                if (deaths <= 4 || match.getQueue() == GameQueueType.ARAM) {
                    break;
                }
                if (((kills + (assists * 0.5)) / deaths) < 0.08) {
                    values.add(ToxicityValue.TROLL);
                    if (win) {
                        values.add(ToxicityValue.TROLLBUTWIN);
                    }
                    break;
                }
                break;
        }
        return values;
    }

    private boolean checkForToxicName(String riotId) {

        return Arrays.stream(toxicNames).anyMatch(riotId.toLowerCase()::contains);
    }
}
