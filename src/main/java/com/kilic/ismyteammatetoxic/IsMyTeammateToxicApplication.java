package com.kilic.ismyteammatetoxic;

import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.kilic.ismyteammatetoxic.config.YAMLConfig;
import com.merakianalytics.orianna.Orianna;

import no.stelar7.api.r4j.basic.cache.impl.FileSystemCacheProvider;
import no.stelar7.api.r4j.basic.calling.DataCall;

@SpringBootApplication
public class IsMyTeammateToxicApplication {

	@Autowired
	static YAMLConfig config;
	static Supplier<FileSystemCacheProvider> fileCache = () -> new FileSystemCacheProvider(20);

	public static void main(String[] args) {
		DataCall.setCacheProvider(new FileSystemCacheProvider());
		SpringApplication.run(IsMyTeammateToxicApplication.class, args);
		Orianna.loadConfiguration("config.json");

	}

}