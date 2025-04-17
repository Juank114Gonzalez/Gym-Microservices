package com.gym.icesi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MiembrosApplication {

	public static void main(String[] args) {
		SpringApplication.run(MiembrosApplication.class, args);
	}

}
