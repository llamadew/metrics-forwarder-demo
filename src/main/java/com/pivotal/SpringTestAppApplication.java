package com.pivotal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class SpringTestAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringTestAppApplication.class, args);
	}
}
