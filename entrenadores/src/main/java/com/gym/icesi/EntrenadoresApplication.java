package com.gym.icesi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class EntrenadoresApplication {

	public static void main(String[] args) {
		SpringApplication.run(EntrenadoresApplication.class, args);
	}

}
