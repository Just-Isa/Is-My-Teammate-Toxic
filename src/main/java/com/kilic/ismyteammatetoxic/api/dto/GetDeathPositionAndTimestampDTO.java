package com.kilic.ismyteammatetoxic.api.dto;

import no.stelar7.api.r4j.pojo.lol.match.v5.TimelinePosition;

public record GetDeathPositionAndTimestampDTO(
        TimelinePosition position,
        long timestamp) {
    public static GetDeathPositionAndTimestampDTO from(
            TimelinePosition position, long timestamp) {
        return new GetDeathPositionAndTimestampDTO(position, timestamp);
    }
}
