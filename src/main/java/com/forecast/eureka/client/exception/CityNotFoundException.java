package com.forecast.eureka.client.exception;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "City Not Found")
public class CityNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String message;
	private int statusCode;

	public CityNotFoundException() {
		super();
	}

	public CityNotFoundException(String msg) {
		super(msg);
		this.message = msg;
	}

	public CityNotFoundException(String msg, int code) {
		super(msg);
		this.message = msg;
		this.statusCode = code;
	}

	public CityNotFoundException(String msg, int code, boolean isJsonStr) {
		super(msg);
		this.message = isJsonStr ? extractErrorMessage(msg) : msg;
		this.statusCode = code;
	}

	public String extractErrorMessage(String errMsg) {
		var mapper = new ObjectMapper();
		try {
			var readValue = mapper.readValue(errMsg, Map.class);
			return (String) readValue.get("message");
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return errMsg;
		}
	}

}