package com.kilic.ismyteammatetoxic;

import java.util.function.Supplier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.merakianalytics.orianna.Orianna;

import no.stelar7.api.r4j.basic.cache.impl.FileSystemCacheProvider;
import no.stelar7.api.r4j.basic.calling.DataCall;

@SpringBootApplication
public class IsMyTeammateToxicApplication {

	static Supplier<FileSystemCacheProvider> fileCache = () -> new FileSystemCacheProvider(20);

	public static void main(String[] args) {
		DataCall.setCacheProvider(new FileSystemCacheProvider());
		SpringApplication.run(IsMyTeammateToxicApplication.class, args);
		Orianna.loadConfiguration("config.json");

	}

}
