package com.forecast.eureka.client.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The Class WeatherMapDTO.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class WeatherMapDTO implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The cod. */
	@JsonProperty("cod")
	private String cod;

	/** The message. */
	@JsonProperty("message")
	private BigDecimal message;

	/** The count. */
	@JsonProperty("cnt")
	private Integer cnt;

	/** The list. */
	@JsonProperty(value = "list")
	private List<WeatherMapTimeDTO> list;

}