package com.websystique.springmvc.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties
public class Results {

	private int stops;
	private List<ResultFlightDet> legs = new ArrayList<ResultFlightDet>();
	
	public Results(){}
	
	public Results(int stops, List<ResultFlightDet> legs) {
		super();
		this.stops = stops;
		this.legs = legs;
	}
	
	public int getStops() {
		return stops;
	}
	public void setStops(int stops) {
		this.stops = stops;
	}
	public List<ResultFlightDet> getLegs() {
		return legs;
	}
	public void setLegs(List<ResultFlightDet> legs) {
		this.legs = legs;
	}
}
