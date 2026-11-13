package com.banking.aiassistant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AiAssistantServiceApplication {

	public static void main(String[] args) {
		
		System.out.println("Truststore: "
		        + System.getProperty("javax.net.ssl.trustStore"));

		System.out.println("Java Home: "
		        + System.getProperty("java.home"));
		SpringApplication.run(AiAssistantServiceApplication.class, args);
	}

}
