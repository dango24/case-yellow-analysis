package com.icarusrises.caseyellowanalysis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry
@SpringBootApplication
public class CaseYellowAnalysisApplication {

	public static void main(String[] args) {
		SpringApplication.run(CaseYellowAnalysisApplication.class, args);
	}
}
