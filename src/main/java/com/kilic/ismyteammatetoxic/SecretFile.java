package com.kilic.ismyteammatetoxic;

import org.springframework.beans.factory.annotation.Value;
import no.stelar7.api.r4j.basic.APICredentials;

public class SecretFile {

    public static String LEAGUE_KEY;
    public static final String TOURNAMENT_KEY = "NO_KEY_:(";
    public static final String LOR_KEY = "NO_KEY_:(";
    public static final String TFT_KEY = "NO_KEY_:(";
    public static final String VAL_KEY = "NO_KEY_:(";

    @Value("${riot_api_key}")
    public void setLeagueKey(String name) {
        LEAGUE_KEY = name;
    }

    public static String getKey(){
        return LEAGUE_KEY;
    }

    public static final APICredentials CREDS = new APICredentials(getKey(), TOURNAMENT_KEY, TFT_KEY, LOR_KEY,
            VAL_KEY);
}
