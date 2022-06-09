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
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.forecast.eureka.client.model.WeatherAverageDTO;
import com.forecast.eureka.client.model.WeatherMapDTO;
import com.forecast.eureka.client.model.WeatherMapTimeDTO;

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
	
	public ResponseEntity<String> getForecastInfo(@NonNull String city) {
		try {
			String response = restTemplate.getForObject(completeUrl(city), String.class);
			return new ResponseEntity<String>(response, HttpStatus.OK);
		} catch (HttpClientErrorException ex) {
			return new ResponseEntity<String>(ex.getResponseBodyAsString(), ex.getStatusCode());
		} catch (Exception ex) {
			return new ResponseEntity<String>(ex.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
		}
	}

	public ResponseEntity<?> weatherForecastAverage(@NonNull String city) {
		var result = new ArrayList<WeatherAverageDTO>();
		try {
			WeatherMapDTO weatherMap = restTemplate.getForObject(completeUrl(city), WeatherMapDTO.class);
			for (LocalDate reference = LocalDate.now(); reference.isBefore(LocalDate.now().plusDays(3)); reference = reference.plusDays(1)) {
				final LocalDate ref = reference;
				List<WeatherMapTimeDTO> collect = weatherMap.getList().stream().filter(x -> x.getDt().toLocalDate().equals(ref)).collect(Collectors.toList());
				if (!CollectionUtils.isEmpty(collect)) {
					result.add(this.average(collect));
				}
			}
		} catch (HttpClientErrorException ex) {
			LOG.error("Exception occurred while processing the request. Exception: {}", ex.getMessage());
			return new ResponseEntity<>(ex.getResponseBodyAsString(), ex.getStatusCode());
		} catch (Exception ex) {
			return new ResponseEntity<String>(ex.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	private WeatherAverageDTO average(List<WeatherMapTimeDTO> list) {
		var result = new WeatherAverageDTO();
		for (WeatherMapTimeDTO item : list) {
			result.setDate(item.getDt().toLocalDate());
			result.plusMap(item);
		}
		result.totalize();
		return result;
	}

	private String completeUrl(String city) {
		UriComponents uriComponents = UriComponentsBuilder.newInstance().scheme("http").host(FORECAST_URL).path("")
				.query("q={keyword}&appid={appid}&cnt={count}&units=metric").buildAndExpand(city, API_KEY, COUNT);
		return uriComponents.toString();
	}
}

// @formatter:on