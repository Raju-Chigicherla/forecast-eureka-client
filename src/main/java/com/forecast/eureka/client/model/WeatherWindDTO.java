package com.forecast.eureka.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// @formatter:off

/**
 * The Class WeatherWindDTO.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class WeatherWindDTO {

	/** The wind speed. */
	@JsonProperty("speed")
	private double speed;

	/** The degree. */
	@JsonProperty("deg")
	private int deg;

	/** The gust. */
	@JsonProperty("gust")
	private double gust;
}

// @formatter:on