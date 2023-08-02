package com.kilic.ismyteammatetoxic.api.dto;

import java.util.List;
import com.kilic.ismyteammatetoxic.domain.ToxicityValue;

public record GetToxicityDTO(
        float toxicityLevel,
        List<ToxicityValue> toxicityValues,
        List<GetDeathPositionAndTimestampDTO> deathsPre10min,
        List<GetDeathPositionAndTimestampDTO> deathsPost10minPre2min,
        List<GetDeathPositionAndTimestampDTO> deaths2minBeforeEnd,
        int amountBaitPings) {
    public static GetToxicityDTO from(float toxicityLevel,
            List<ToxicityValue> toxicityValues,
            List<GetDeathPositionAndTimestampDTO> deathsPre10min,
            List<GetDeathPositionAndTimestampDTO> deathsPost10minPre2min,
            List<GetDeathPositionAndTimestampDTO> deaths2minBeforeEnd,
            int amountBaitPings) {
        return new GetToxicityDTO(toxicityLevel, toxicityValues, deathsPre10min, deathsPost10minPre2min,
                deaths2minBeforeEnd, amountBaitPings);
    }
}
