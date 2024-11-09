package com.kilic.ismyteammatetoxic;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import no.stelar7.api.r4j.basic.APICredentials;

public class SecretFile {

    public static String LEAGUE_KEY = System.getenv("riot_api_key");


    public final static APICredentials CREDS = new APICredentials(LEAGUE_KEY);
}
