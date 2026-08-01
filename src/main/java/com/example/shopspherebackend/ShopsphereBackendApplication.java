package com.example.shopspherebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ShopsphereBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopsphereBackendApplication.class, args);
	}

}
