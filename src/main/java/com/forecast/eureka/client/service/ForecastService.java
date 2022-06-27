package com.forecast.eureka.client.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.forecast.eureka.client.constants.AppConstants;
import com.forecast.eureka.client.exception.CityNotFoundException;
import com.forecast.eureka.client.model.WeatherAverageDTO;
import com.forecast.eureka.client.model.WeatherDTO;
import com.forecast.eureka.client.model.WeatherMapDTO;
import com.forecast.eureka.client.model.WeatherMapTimeDTO;
import com.forecast.eureka.client.util.AppUtil;

import lombok.NonNull;

// @formatter:off

@Service
public class ForecastService {

	private static final Logger LOG = LoggerFactory.getLogger(ForecastService.class);

	@Value("${forecast.url:api.openweathermap.org/data/2.5/forecast}")
	private String FORECAST_URL;

	@Value("${forecast.apikey:}")
	private String API_KEY;

	@Value("${forecast.count:32}")
	private int COUNT;

	@Autowired
	private RestTemplate restTemplate;

	public ResponseEntity<String> getForecastInfo(@NonNull String city, @Nullable String days) throws Exception {
		try {
			int noOfDays = !StringUtils.isBlank(days) && Integer.valueOf(days) >= 1 ? (Integer.valueOf(days) * 8) : COUNT;
			String response = restTemplate.getForObject(AppUtil.completeUrl(city, FORECAST_URL, API_KEY, noOfDays), String.class);
			return new ResponseEntity<String>(response, HttpStatus.OK);
		} catch (HttpClientErrorException ex) {
			throw new CityNotFoundException(ex.getResponseBodyAsString(), ex.getStatusCode().value(), true);
		} catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
	}

	public ResponseEntity<Map<String, Object>> weatherForecastAverage(@NonNull String city) throws Exception {
		var resultMap = new HashMap<String, Object>();
		var result = new ArrayList<WeatherAverageDTO>();
		StringBuilder weatherMessage = new StringBuilder(AppConstants.EMPTY_STRING);
		try {
			WeatherMapDTO weatherMap = restTemplate.getForObject(AppUtil.completeUrl(city, FORECAST_URL, API_KEY, COUNT), WeatherMapDTO.class);

			for (LocalDate reference = LocalDate.now(); reference.isBefore(LocalDate.now().plusDays(AppConstants.MAX_NEXT_DAYS)); reference = reference.plusDays(1)) {
				final LocalDate ref = reference;
				
				if (ref.equals(LocalDate.now())) {
					Predicate<WeatherMapTimeDTO> predicate = x -> x.getDt().getHour() >= LocalDateTime.now().getHour() && x.getDt().getHour() <= (LocalDateTime.now().getHour() + 3);
					Optional<WeatherMapTimeDTO> currentWeather = weatherMap.getList().stream().filter(x -> x.getDt().toLocalDate().equals(ref)).filter(predicate).findFirst();

					if (currentWeather.isPresent()) {
						final WeatherMapTimeDTO todaysWeather = currentWeather.get();
						WeatherDTO weather = todaysWeather.getWeather().get(0);
						weatherMessage.setLength(0);
						// Rain Check
						if (weather.getMain().toLowerCase().contains("rain") || weather.getDescription().toLowerCase().contains("rain")) {
							LOG.info("It's raining. Please carry umbrella. {}", AppConstants.MSG_RAIN_EXPECTED);
							weatherMessage.append(AppConstants.MSG_RAIN_EXPECTED);
						} else if (todaysWeather.getMain().getTemp().compareTo(BigDecimal.valueOf(AppConstants.MAX_TEMP)) == 1) {
							// Temperature
							LOG.info("Temparature is above 40 degree celcius. {}", AppConstants.MSG_TEMP_40_DEGREES_ABOVE);
							weatherMessage.append(AppConstants.MSG_TEMP_40_DEGREES_ABOVE);
						} else if (BigDecimal.valueOf(todaysWeather.getWind().getSpeed()).compareTo(BigDecimal.valueOf(AppConstants.MAX_WIND_SPEED)) == 1) {
							// Wind speed
							LOG.info("Wind speed is too high. {}", AppConstants.MSG_WIND_SPEED_MORE_THAN_10_KMPH);
							weatherMessage.append(AppConstants.MSG_WIND_SPEED_MORE_THAN_10_KMPH);
						} else {
							weatherMessage.append(AppConstants.MSG_NORMAL);
						}
					}
				} else {
					List<WeatherMapTimeDTO> collect = weatherMap.getList().stream().filter(x -> x.getDt().toLocalDate().equals(ref)).collect(Collectors.toList());
					if (!CollectionUtils.isEmpty(collect)) {
						result.add(AppUtil.average(collect));
					}
				}
			}
			resultMap.put(AppConstants.WEATHER_AVERAGE, result);			
			resultMap.put(AppConstants.WEATHER_MESSAGE, weatherMessage.toString());			
		} catch (HttpClientErrorException ex) {
			LOG.error("Exception occurred while processing the request. Exception: {}", ex.getMessage());
			throw new CityNotFoundException(ex.getResponseBodyAsString(), ex.getStatusCode().value(), true);
		} catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	}

}

// @formatter:on