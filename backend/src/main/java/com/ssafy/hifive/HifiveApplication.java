package com.ssafy.hifive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableCaching
@EnableJpaAuditing
@EnableScheduling
@SpringBootApplication
public class HifiveApplication {

	public static void main(String[] args) {
		SpringApplication.run(HifiveApplication.class, args);
	}
}
