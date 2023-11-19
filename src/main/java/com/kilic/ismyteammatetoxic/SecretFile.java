package com.kilic.ismyteammatetoxic;

import org.springframework.beans.factory.annotation.Value;

import no.stelar7.api.r4j.basic.APICredentials;

public class SecretFile {


    @Value("${riot_api_key}")
    public static String LEAGUE_KEY;
    public static final String TOURNAMENT_KEY = "NO_KEY_:(";
    public static final String LOR_KEY = "NO_KEY_:(";
    public static final String TFT_KEY = "NO_KEY_:(";
    public static final String VAL_KEY = "NO_KEY_:(";

    public static final APICredentials CREDS = new APICredentials(LEAGUE_KEY, TOURNAMENT_KEY, TFT_KEY, LOR_KEY,
            VAL_KEY);
}
