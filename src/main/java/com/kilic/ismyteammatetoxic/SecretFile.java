package com.kilic.ismyteammatetoxic;


import no.stelar7.api.r4j.basic.APICredentials;

public class SecretFile {

    public static String LEAGUE_KEY = System.getenv("riot_api_key");
    public static final String TOURNAMENT_KEY = "NO_KEY_:(";
    public static final String LOR_KEY = "NO_KEY_:(";
    public static final String TFT_KEY = "NO_KEY_:(";
    public static final String VAL_KEY = "NO_KEY_:(";

    public static final APICredentials CREDS = new APICredentials(LEAGUE_KEY);
}
