package com.kilic.ismyteammatetoxic;

import no.stelar7.api.r4j.basic.APICredentials;

public class SecretFile {

    public static final String LEAGUE_KEY = "RGAPI-96f0a4c2-cefb-4ca6-9ac7-6d5c7b2e0cd8";
    public static final String TOURNAMENT_KEY = "NO_KEY_:(";
    public static final String LOR_KEY = "NO_KEY_:(";
    public static final String TFT_KEY = "NO_KEY_:(";
    public static final String VAL_KEY = "NO_KEY_:(";

    public static final APICredentials CREDS = new APICredentials(LEAGUE_KEY, TOURNAMENT_KEY, TFT_KEY, LOR_KEY,
            VAL_KEY);
}
