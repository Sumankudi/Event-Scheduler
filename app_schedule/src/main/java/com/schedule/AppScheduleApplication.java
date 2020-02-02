package com.schedule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class AppScheduleApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppScheduleApplication.class, args);
	}

}
