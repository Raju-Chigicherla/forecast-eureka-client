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
import com.forecast.eureka.client.constants.AppConstants;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

// @formatter:off

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

	/** The wind daily. */
	private BigDecimal windDaily;

	/** The nightly. */
	private BigDecimal nightly;

	/** The wind nightly. */
	private BigDecimal windNightly;

	/** The pressure. */
	private BigDecimal pressure;
	
	/** The message. */
	private String message;

	/** The total daily. */
	@JsonIgnore
	private BigDecimal totalDaily;

	/** The total wind daily. */
	@JsonIgnore
	private BigDecimal totalWindDaily;

	/** The quantitative daily. */
	@JsonIgnore
	private Integer quantDaily;

	/** The quantitative wind daily. */
	@JsonIgnore
	private Integer quantWindDaily;

	/** The total nightly. */
	@JsonIgnore
	private BigDecimal totalNightly;

	/** The total wind nightly. */
	@JsonIgnore
	private BigDecimal totalWindNightly;

	/** The quantitative nightly. */
	@JsonIgnore
	private Integer quantNightly;

	/** The quantitative wind nightly. */
	@JsonIgnore
	private Integer quantWindNightly;

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
		this.totalWindDaily = BigDecimal.ZERO;
		this.totalNightly = BigDecimal.ZERO;
		this.totalWindNightly = BigDecimal.ZERO;
		this.totalPressure = BigDecimal.ZERO;
		this.quantDaily = 0;
		this.quantWindDaily = 0;
		this.quantNightly = 0;
		this.quantWindNightly = 0;
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
			this.totalWindDaily = this.totalWindDaily.add(BigDecimal.valueOf(map.getWind().getSpeed()));
			this.quantDaily++;
			this.quantWindDaily++;
		} else {
			this.totalNightly = this.totalNightly.add(map.getMain().getTemp());
			this.totalWindNightly = this.totalWindNightly.add(BigDecimal.valueOf(map.getWind().getSpeed()));
			this.quantNightly++;
			this.quantWindNightly++;
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

		this.windDaily = (this.quantWindDaily > 0)
				? this.totalWindDaily.divide(new BigDecimal(this.quantWindDaily.toString()), 2, RoundingMode.HALF_UP)
				: null;
		this.windNightly = (this.quantWindNightly > 0)
				? this.totalWindNightly.divide(new BigDecimal(this.quantWindNightly.toString()), 2, RoundingMode.HALF_UP)
				: null;

		this.pressure = (this.quantPressure > 0)
				? this.totalPressure.divide(new BigDecimal(this.quantPressure.toString()), 2, RoundingMode.HALF_UP)
				: null;
	}
	
	/**
	 * Adds the weather message.
	 *
	 * @return the weather message
	 */
	public void addWeatherMessage() {
		if(this.daily.compareTo(BigDecimal.valueOf(AppConstants.MAX_TEMP)) == 1) {
			this.message = AppConstants.MSG_TEMP_40_DEGREES_ABOVE;
		} else if(this.windDaily.compareTo(BigDecimal.valueOf(AppConstants.MAX_WIND_SPEED)) == 1) {
			this.message = AppConstants.MSG_WIND_SPEED_MORE_THAN_10_KMPH;
		} else {
			this.message = AppConstants.MSG_NORMAL;
		}
	}

}

// @formatter:on