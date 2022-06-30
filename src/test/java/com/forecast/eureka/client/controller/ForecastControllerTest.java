package com.forecast.eureka.client.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import com.forecast.eureka.client.constants.AppConstants;
import com.forecast.eureka.client.model.WeatherAverageDTO;
import com.forecast.eureka.client.service.ForecastService;

@WebMvcTest(ForecastController.class)
class ForecastControllerTest {

	private static final Logger LOG = LoggerFactory.getLogger(ForecastControllerTest.class);

	@MockBean
	ForecastService forecastService;

	@Autowired
	MockMvc mockMvc;

	@Test
	public void testWeatherForecastAverage() throws Exception {

		var result = new ArrayList<WeatherAverageDTO>();
		WeatherAverageDTO obj1 = new WeatherAverageDTO();
		WeatherAverageDTO obj2 = new WeatherAverageDTO();
		WeatherAverageDTO obj3 = new WeatherAverageDTO();

		result.add(obj1);
		result.add(obj2);
		result.add(obj3);
		Map<String, Object> testmap = new HashMap<>();
		testmap.put(AppConstants.WEATHER_AVERAGE, result);
		testmap.put(AppConstants.WEATHER_MESSAGE, "Testing");

		ResponseEntity<Map<String, Object>> testData = new ResponseEntity<>(testmap, HttpStatus.OK);
		Mockito.when(forecastService.weatherForecastAverage(Mockito.anyString())).thenReturn(testData);
		mockMvc.perform(get("/forecast").param("city", "Bengaluru")).andExpect(status().isOk());
	}

	@Test
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void testWeatherForecastAverage_1() throws Exception {
		LOG.info("Checking the City not found exception");
		ResponseEntity testData = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		Mockito.when(forecastService.weatherForecastAverage(Mockito.anyString())).thenReturn(testData);
		mockMvc.perform(get("/forecast")).andExpect(status().isBadRequest());
	}

}