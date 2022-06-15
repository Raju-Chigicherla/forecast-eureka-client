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
 * The Class WeatherDTO.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class WeatherDTO {

	/** The id. */
	@JsonProperty("id")
	private int id;

	/** The main. */
	@JsonProperty("main")
	private String main;

	/** The description. */
	@JsonProperty("description")
	private String description;

	/** The icon. */
	@JsonProperty("icon")
	private String icon;
}

// @formatter:on