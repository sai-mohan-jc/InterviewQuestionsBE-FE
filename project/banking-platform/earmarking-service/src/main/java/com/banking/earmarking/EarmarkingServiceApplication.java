package com.banking.earmarking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class EarmarkingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EarmarkingServiceApplication.class, args);
	}

}
