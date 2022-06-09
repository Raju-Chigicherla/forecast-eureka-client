package com.forecast.eureka.client.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.forecast.eureka.client.service.ForecastService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
public class ForecastController {
	
	private static final Logger LOG = LoggerFactory.getLogger(ForecastController.class);
	
	/** The forecast service. */
	@Autowired
	private ForecastService forecastService;
	
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
	
	/**
	 * Weather forecast average.
	 *
	 * @param city the city
	 * @return the response entity
	 */
	@Operation(summary = "This is to fetch next 3 days Weather forecast")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Details of Next 3 days Weather Forecast", content = { @Content(mediaType = "application/json") }),
	})
	@GetMapping(value = "/forecast", produces = MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView weatherForecastAverage(@RequestParam(required = true, name = "city") String city) {
		ResponseEntity<?> forecastAvg = forecastService.weatherForecastAverage(city);
		Object response = forecastAvg.getBody();
		
		ModelAndView mv = new ModelAndView("weather-data");
		mv.addObject("weatherDetails", response);
		return mv;
//		return rforecastService.weatherForecastAverage(city);
	}

	/**
	 * Gets the forecast data.
	 *
	 * @param city the city
	 * @return the forecast data
	 */
	@Operation(summary = "This is to fetch next 3 days Weather forecast")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Details of Next 3 days Weather Forecast", content = { @Content(mediaType = "application/json") }),
	})
	@GetMapping(value = "/getForecast", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getForecastData(@RequestParam(required = true, name = "city") String city) {
		return forecastService.getForecastInfo(city);
	}
}