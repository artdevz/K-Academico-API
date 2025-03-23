package com.kacademic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class KacademicApplication {

	public static void main(String[] args) {
		SpringApplication.run(KacademicApplication.class, args);
	}

}
