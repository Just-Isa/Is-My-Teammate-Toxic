package com.kilic.ismyteammatetoxic.api.dto;

import java.util.List;
import com.kilic.ismyteammatetoxic.domain.ToxicityValue;

public record GetToxicityDTO(
        float toxicityLevel,
        List<ToxicityValue> toxicityValues) {
    public static GetToxicityDTO from(float toxicityLevel, List<ToxicityValue> toxicityValues) {
        return new GetToxicityDTO(toxicityLevel, toxicityValues);
    }
}
