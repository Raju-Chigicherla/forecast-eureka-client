package com.forecast.eureka.client.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class ForecastController {
	
	private static final Logger LOG = LoggerFactory.getLogger(ForecastController.class);
	
	@GetMapping("/")
	public ModelAndView homePage() {
		LOG.info("Inside ForecastController homePage() method!!!");
		return new ModelAndView("index");
	}

	@GetMapping("/hello-worlds/{name}")
	public String getHelloWorld(@PathVariable String name) {
		LOG.info("Inside ForecastController getHelloWorld() method!!!");
		return "Hello World " + name;
	}
}