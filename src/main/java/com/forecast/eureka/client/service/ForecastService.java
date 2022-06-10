package com.forecast.eureka.client.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.forecast.eureka.client.exception.CityNotFoundException;
import com.forecast.eureka.client.model.WeatherAverageDTO;
import com.forecast.eureka.client.model.WeatherMapDTO;
import com.forecast.eureka.client.model.WeatherMapTimeDTO;
import com.forecast.eureka.client.util.AppUtil;

import lombok.NonNull;

// @formatter:off

@Service
public class ForecastService {

	private static final Logger LOG = LoggerFactory.getLogger(ForecastService.class);
	
	@Value("${forecast.url:}")
	private String FORECAST_URL;

	@Value("${forecast.apikey:}")
	private String API_KEY;

	@Value("${forecast.count:24}")
	private int COUNT;

	@Autowired
	private RestTemplate restTemplate;
	
	public ResponseEntity<String> getForecastInfo(@NonNull String city) throws Exception {
		try {
			String response = restTemplate.getForObject(AppUtil.completeUrl(city, FORECAST_URL, API_KEY, COUNT), String.class);
			return new ResponseEntity<String>(response, HttpStatus.OK);
		} catch (HttpClientErrorException ex) {
			throw new CityNotFoundException(ex.getResponseBodyAsString(), ex.getStatusCode().value(), true);
		} catch (Exception ex) {
			throw new Exception(ex.getMessage()); 
		}
	}

	public ResponseEntity<?> weatherForecastAverage(@NonNull String city) throws Exception {
		var result = new ArrayList<WeatherAverageDTO>();
		try {
			WeatherMapDTO weatherMap = restTemplate.getForObject(AppUtil.completeUrl(city, FORECAST_URL, API_KEY, COUNT), WeatherMapDTO.class);
			for (LocalDate reference = LocalDate.now(); reference.isBefore(LocalDate.now().plusDays(3)); reference = reference.plusDays(1)) {
				final LocalDate ref = reference;
				List<WeatherMapTimeDTO> collect = weatherMap.getList().stream().filter(x -> x.getDt().toLocalDate().equals(ref)).collect(Collectors.toList());
				if (!CollectionUtils.isEmpty(collect)) {
					result.add(AppUtil.average(collect));
				}
			}
		} catch (HttpClientErrorException ex) {
			LOG.error("Exception occurred while processing the request. Exception: {}", ex.getMessage());
			throw new CityNotFoundException(ex.getResponseBodyAsString(), ex.getStatusCode().value(), true);
		} catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
}

// @formatter:on