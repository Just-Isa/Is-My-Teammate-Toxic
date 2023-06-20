package com.kilic.ismyteammatetoxic.api.dto;

public record GetRelevantPlayerInfoDTO(String champName) {
    public static GetRelevantPlayerInfoDTO from(String champString) {
        return new GetRelevantPlayerInfoDTO(champString);
    }

}
