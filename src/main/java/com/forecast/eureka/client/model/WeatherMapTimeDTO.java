package com.forecast.eureka.client.model;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The Class WeatherMapTimeDTO.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class WeatherMapTimeDTO {

	/** The date. */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonProperty("dt_txt")
	private LocalDateTime dt;

	/** The main info. */
	@JsonProperty("main")
	private WeatherMapTimeMainDTO main;

	/** The wind. */
	@JsonProperty("wind")
	private WeatherWindDTO wind;
	
	@JsonProperty("weather")
	private List<WeatherDTO> weather;

	/**
	 * Checks if is day time.
	 *
	 * @return the boolean
	 */
	@JsonIgnore
	public Boolean isDayTime() {
		return (this.dt.getHour() >= 6 && this.dt.getHour() < 18);
	}

}