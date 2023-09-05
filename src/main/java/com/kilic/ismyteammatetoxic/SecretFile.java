package com.kilic.ismyteammatetoxic;

import no.stelar7.api.r4j.basic.APICredentials;

public class SecretFile {

    public static final String LEAGUE_KEY = "RGAPI-acd9d3cc-3858-4550-b715-8fe52b13c3fc";
    public static final String TOURNAMENT_KEY = "NO_KEY_:(";
    public static final String LOR_KEY = "NO_KEY_:(";
    public static final String TFT_KEY = "NO_KEY_:(";
    public static final String VAL_KEY = "NO_KEY_:(";

    public static final APICredentials CREDS = new APICredentials(LEAGUE_KEY, TOURNAMENT_KEY, TFT_KEY, LOR_KEY,
            VAL_KEY);
}