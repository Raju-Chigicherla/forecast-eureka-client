package com.forecast.eureka.client.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.forecast.eureka.client.constants.AppConstants;
import com.forecast.eureka.client.exception.CityNotFoundException;
import com.forecast.eureka.client.model.WeatherAverageDTO;
import com.forecast.eureka.client.service.ForecastService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

// @formatter:off

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

	@GetMapping("/helloworld/{name}")
	public String getHelloWorld(@PathVariable String name) {
		LOG.info("Inside ForecastController getHelloWorld() method!!!");
		return "Hello World " + name;
	}

	/**
	 * Weather forecast average.
	 *
	 * @param city the city
	 * @return the response entity
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Operation(summary = "This is to fetch next 3 days Weather forecast")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Details of Next 3 days Weather Forecast", content = {@Content(mediaType = "application/json") }), })
	@GetMapping(value = "/forecast", produces = MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView weatherForecastAverage(@RequestParam(required = true, name = "city") String city) throws Exception {
		ResponseEntity<Map<String, Object>> weatherResponse = forecastService.weatherForecastAverage(city);
		Map<String, Object> responseBody = weatherResponse.getBody();
		ModelAndView mv = new ModelAndView("weather-data");
		mv.addObject("weatherDetails", (List<WeatherAverageDTO>) responseBody.get(AppConstants.WEATHER_AVERAGE));
		mv.addObject("weatherMsg", (String) responseBody.get(AppConstants.WEATHER_MESSAGE));
		return mv;
	}

	/**
	 * Gets the forecast data.
	 *
	 * @param city the city
	 * @return the forecast data
	 * @throws Exception
	 */
	@Operation(summary = "This is to fetch next 3 days Weather forecast")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Details of Next 3 days Weather Forecast", content = {@Content(mediaType = "application/json") }), })
	@GetMapping(value = "/getForecast", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getForecastData(@RequestParam(required = true, name = "city") String city, @RequestParam(required = false, name = "days") String days) throws Exception {
		return forecastService.getForecastInfo(city, days);
	}

	@ExceptionHandler(CityNotFoundException.class)
	public ModelAndView handleEmployeeNotFoundException(HttpServletRequest request, Exception ex) {
		LOG.error("Requested URL: {}, Path: {}", request.getRequestURL(), request.getPathInfo());
		LOG.error("Exception Raised: {}", ex);

		ModelAndView mvc = new ModelAndView("error");
		mvc.addObject("message", ex.getMessage());
		mvc.addObject("url", request.getRequestURL());

		return mvc;
	}
}

// @formatter:on