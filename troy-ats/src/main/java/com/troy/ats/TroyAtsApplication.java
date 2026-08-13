package com.troy.ats;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.ZoneId;

@SpringBootApplication
public class TroyAtsApplication {

	public static void main(String[] args) {
		SpringApplication.run(TroyAtsApplication.class, args);
	}

	public static ZoneId getTimeZone(String countryCode) {

		return switch (countryCode.toUpperCase()) {
			case "IN" -> ZoneId.of("Asia/Kolkata");
			case "GB", "UK" -> ZoneId.of("Europe/London");
			default -> throw new IllegalArgumentException(
					"Unsupported country code: " + countryCode
			);
		};
	}

}
