package com.websystique.springmvc.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties
public class FlightDay {

	@JsonProperty("day")
	private int day;
	@JsonProperty("flights")
	public List<Flight> flights;
	
	
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public List<Flight> getFlights() {
		return flights;
	}
	public void setFlights(List<Flight> flights) {
		this.flights = flights;
	}
	
	
	
}
