package com.websystique.springmvc.model;

import java.util.List;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties
public class FlightsMonth {

	@JsonProperty("month")
	private String month;
	
	@JsonProperty("days")
	public List<FlightDay> days;

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public List<FlightDay> getDays() {
		return days;
	}

	public void setDays(List<FlightDay> days) {
		this.days = days;
	}
	
}
