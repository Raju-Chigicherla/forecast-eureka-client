package com.forecast.eureka.client.util;

import java.util.List;

import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.forecast.eureka.client.model.WeatherAverageDTO;
import com.forecast.eureka.client.model.WeatherMapTimeDTO;

// @formatter:off

/**
 * The Class AppUtil.
 */
public class AppUtil {

	/**
	 * Instantiates a new application utility.
	 */
	private AppUtil() {

	}

	/**
	 * Average of the Weather - Temperature & Wind
	 *
	 * @param list the list of {@link WeatherMapTimeDTO}
	 * @return the weather average DTO
	 */
	public static WeatherAverageDTO average(List<WeatherMapTimeDTO> list) {
		var result = new WeatherAverageDTO();
		for (WeatherMapTimeDTO item : list) {
			result.setDate(item.getDt().toLocalDate());
			result.plusMap(item);
		}
		result.totalize();
		result.addWeatherMessage();
		return result;
	}

	/**
	 * Complete URL.
	 *
	 * @param city        the city
	 * @param forecastUrl the forecast URL
	 * @param apiKey      the API key
	 * @param count       the count
	 * @return the string
	 */
	public static String completeUrl(String city, String forecastUrl, String apiKey, int count) {
		UriComponents uriComponents = UriComponentsBuilder.newInstance().scheme("http").host(forecastUrl).path("")
				.query("q={keyword}&appid={appid}&cnt={count}&units=metric").buildAndExpand(city, apiKey, count);
		return uriComponents.toString();
	}
	
}

// @formatter:on