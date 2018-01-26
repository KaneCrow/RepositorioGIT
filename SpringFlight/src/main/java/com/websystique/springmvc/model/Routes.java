package com.websystique.springmvc.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties
public class Routes {
	
	private String airportFrom;
	private String airportTo;
	private String connectingAirport;
	private Boolean newRoute;
	private Boolean seasonalRoute;
	private String group;
	
	public Routes(){}
	
	public Routes(String airportFrom, String airportTo, String connectingAirport) {
		super();
		this.airportFrom = airportFrom;
		this.airportTo = airportTo;
		this.connectingAirport = connectingAirport;
	}
	
	public Routes(String airportFrom, String airportTo, String connectingAirport, Boolean newRoute,
			Boolean seasonalRoute, String group) {
		super();
		this.airportFrom = airportFrom;
		this.airportTo = airportTo;
		this.connectingAirport = connectingAirport;
		this.newRoute = newRoute;
		this.seasonalRoute = seasonalRoute;
		this.group = group;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((airportFrom == null) ? 0 : airportFrom.hashCode());
		result = prime * result + ((airportTo == null) ? 0 : airportTo.hashCode());
		result = prime * result + ((connectingAirport == null) ? 0 : connectingAirport.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Routes other = (Routes) obj;
		if (airportFrom == null) {
			if (other.airportFrom != null)
				return false;
		} else if (!airportFrom.equals(other.airportFrom))
			return false;
		if (airportTo == null) {
			if (other.airportTo != null)
				return false;
		} else if (!airportTo.equals(other.airportTo))
			return false;
		if (connectingAirport == null) {
			if (other.connectingAirport != null)
				return false;
		} else if (!connectingAirport.equals(other.connectingAirport))
			return false;
		return true;
	}

	public String getAirportFrom() {
		return airportFrom;
	}
	public void setAirportFrom(String airportFrom) {
		this.airportFrom = airportFrom;
	}
	public String getAirportTo() {
		return airportTo;
	}
	public void setAirportTo(String airportTo) {
		this.airportTo = airportTo;
	}
	public String getConnectingAirport() {
		return connectingAirport;
	}
	public void setConnectingAirport(String connectingAirport) {
		this.connectingAirport = connectingAirport;
	}
	public Boolean getNewRoute() {
		return newRoute;
	}
	public void setNewRoute(Boolean newRoute) {
		this.newRoute = newRoute;
	}
	public Boolean getSeasonalRoute() {
		return seasonalRoute;
	}
	public void setSeasonalRoute(Boolean seasonalRoute) {
		this.seasonalRoute = seasonalRoute;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}

}
