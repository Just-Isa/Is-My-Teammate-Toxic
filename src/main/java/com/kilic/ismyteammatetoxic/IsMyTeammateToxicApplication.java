package com.kilic.ismyteammatetoxic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.kilic.ismyteammatetoxic.config.YAMLConfig;
import com.merakianalytics.orianna.Orianna;

import no.stelar7.api.r4j.basic.APICredentials;
import no.stelar7.api.r4j.impl.R4J;

@SpringBootApplication
public class IsMyTeammateToxicApplication {

	@Autowired
	static YAMLConfig config;

	public static void main(String[] args) {
		SpringApplication.run(IsMyTeammateToxicApplication.class, args);

		// ORIANNA
		Orianna.loadConfiguration("config.json");

	}

}