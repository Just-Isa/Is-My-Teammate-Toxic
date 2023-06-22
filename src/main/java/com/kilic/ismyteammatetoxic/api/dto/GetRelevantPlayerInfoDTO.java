package com.kilic.ismyteammatetoxic.api.dto;

import no.stelar7.api.r4j.basic.constants.types.lol.LaneType;

public record GetRelevantPlayerInfoDTO(String champName, int baitpings, int kills, int deaths, int assists,
        LaneType lane,
        boolean win) {
    public static GetRelevantPlayerInfoDTO from(String champString, int baitpings, int kills, int deaths, int assists,
            LaneType lane, boolean win) {
        return new GetRelevantPlayerInfoDTO(champString, baitpings, kills, deaths, assists, lane, win);
    }

}
