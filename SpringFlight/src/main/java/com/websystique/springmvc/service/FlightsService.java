package com.websystique.springmvc.service;

import java.util.List;

import com.websystique.springmvc.model.Results;
import com.websystique.springmvc.model.Routes;

public interface FlightsService {
	
	List<Routes> findAllRouts();
	
	Boolean findOutMyRouteDirect(List<Routes> routes, String departure, String arrival);
	
	List<Results> findMyWayDirect(String departure, String arrival, String departureDateTime, String arrivalDateTime);
	
	List<Results> findMyWayIndirect(List<Routes> routes, String departure, String arrival, String departureDateTime, String arrivalDateTime);
}
