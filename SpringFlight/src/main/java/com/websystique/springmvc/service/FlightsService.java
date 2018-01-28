package com.websystique.springmvc.service;

import java.util.List;

import com.websystique.springmvc.model.Results;
import com.websystique.springmvc.model.Routes;

public interface FlightsService {
	
	/**
	 * Get all routes from API
	 * @return List<Routes>
	 */
	List<Routes> findAllRouts();
	
	/**
	 * Get if there is a direct route from departure location to arrival one 
	 * @param routes
	 * @param departure
	 * @param arrival
	 * @return boolean
	 */
	Boolean findOutMyRouteDirect(List<Routes> routes, String departure, String arrival);
	
	/**
	 * Get every available direct flight from departure location to arrival one between specified times for departure and arrival
	 * @param departure
	 * @param arrival
	 * @param departureDateTime
	 * @param arrivalDateTime
	 * @return List<Results>
	 */
	List<Results> findMyWayDirect(String departure, String arrival, String departureDateTime, String arrivalDateTime);
	
	/**
	 * Get every available flight with one stop from departure location to arrival one between specified times for departure and arrival
	 * @param routes
	 * @param departure
	 * @param arrival
	 * @param departureDateTime
	 * @param arrivalDateTime
	 * @return List<Results>
	 */
	List<Results> findMyWayIndirect(List<Routes> routes, String departure, String arrival, String departureDateTime, String arrivalDateTime);
}
