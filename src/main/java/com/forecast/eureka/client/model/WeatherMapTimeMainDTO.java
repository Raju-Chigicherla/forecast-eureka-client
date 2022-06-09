package com.forecast.eureka.client.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The Class WeatherMapTimeMainDTO.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class WeatherMapTimeMainDTO {

	/** The temperature. */
	@JsonProperty("temp")
	private BigDecimal temp;

	/** The minimum temperature. */
	@JsonProperty("temp_min")
	private BigDecimal temp_min;

	/** The maximum temperature. */
	@JsonProperty("temp_max")
	private BigDecimal temp_max;

	/** The pressure. */
	@JsonProperty("pressure")
	private BigDecimal pressure;

	/** The sea level. */
	@JsonProperty("sea_level")
	private BigDecimal sea_level;

	/** The ground level. */
	@JsonProperty("grnd_level")
	private BigDecimal grnd_level;

	/** The humidity. */
	@JsonProperty("humidity")
	private BigDecimal humidity;

	/** The temperature kf. */
	@JsonProperty("temp_kf")
	private BigDecimal temp_kf;

}