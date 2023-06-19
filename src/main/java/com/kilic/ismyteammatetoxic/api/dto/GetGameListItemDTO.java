package com.kilic.ismyteammatetoxic.api.dto;

import java.util.Calendar;

import no.stelar7.api.r4j.pojo.lol.match.v5.LOLMatch;

public record GetGameListItemDTO(String dateOfGame, String gameDuration) {
    public static GetGameListItemDTO from(LOLMatch match) {
        String dur = match.getGameDurationAsDuration()
                .toString()
                .substring(2)
                .replaceAll("(\\d[HMS])(?!$)", "$1 ")
                .toLowerCase();

        Calendar cal = Calendar.getInstance();

        cal.setTimeInMillis(match.getGameStartTimestamp());

        String dateString = Integer.toString(cal.get(Calendar.DAY_OF_MONTH)) + " / "
                + Integer.toString(cal.get(Calendar.MONTH)) + " / "
                + Integer.toString(cal.get(Calendar.YEAR));

        return new GetGameListItemDTO(dateString, dur);
    }
}
