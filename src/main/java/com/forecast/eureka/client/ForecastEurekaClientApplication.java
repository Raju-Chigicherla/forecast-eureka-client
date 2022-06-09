package com.forecast.eureka.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

// @formatter:off

/**
 * The Class ForecastEurekaClientApplication.
 */
@SpringBootApplication
@EnableEurekaClient
public class ForecastEurekaClientApplication {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		SpringApplication.run(ForecastEurekaClientApplication.class, args);
	}

}

// @formatter:on