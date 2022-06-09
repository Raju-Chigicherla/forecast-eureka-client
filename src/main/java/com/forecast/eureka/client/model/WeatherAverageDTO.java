package com.forecast.eureka.client.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * The Class WeatherAverageDTO.
 */
@Data
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class WeatherAverageDTO implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The date. */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate date;

	/** The daily. */
	private BigDecimal daily;

	/** The nightly. */
	private BigDecimal nightly;

	/** The pressure. */
	private BigDecimal pressure;

	/** The total daily. */
	@JsonIgnore
	private BigDecimal totalDaily;

	/** The quantitative daily. */
	@JsonIgnore
	private Integer quantDaily;

	/** The total nightly. */
	@JsonIgnore
	private BigDecimal totalNightly;

	/** The quantitative nightly. */
	@JsonIgnore
	private Integer quantNightly;

	/** The total pressure. */
	@JsonIgnore
	private BigDecimal totalPressure;

	/** The quantitative pressure. */
	@JsonIgnore
	private Integer quantPressure;

	/**
	 * Instantiates a new weather average DTO.
	 */
	public WeatherAverageDTO() {
		this.totalDaily = BigDecimal.ZERO;
		this.totalNightly = BigDecimal.ZERO;
		this.totalPressure = BigDecimal.ZERO;
		this.quantDaily = 0;
		this.quantNightly = 0;
		this.quantPressure = 0;
	}

	/**
	 * Plus map.
	 *
	 * @param map the map
	 */
	public void plusMap(WeatherMapTimeDTO map) {
		if (map.isDayTime()) {
			this.totalDaily = this.totalDaily.add(map.getMain().getTemp());
			this.quantDaily++;
		} else {
			this.totalNightly = this.totalNightly.add(map.getMain().getTemp());
			this.quantNightly++;
		}
		this.totalPressure = this.totalPressure.add(map.getMain().getTemp());
		this.quantPressure++;
	}

	/**
	 * Totals.
	 */
	public void totalize() {
		this.daily = (this.quantDaily > 0)
				? this.totalDaily.divide(new BigDecimal(this.quantDaily.toString()), 2, RoundingMode.HALF_UP)
				: null;
		this.nightly = (this.quantNightly > 0)
				? this.totalNightly.divide(new BigDecimal(this.quantNightly.toString()), 2, RoundingMode.HALF_UP)
				: null;
		this.pressure = (this.quantPressure > 0)
				? this.totalPressure.divide(new BigDecimal(this.quantPressure.toString()), 2, RoundingMode.HALF_UP)
				: null;
	}

}