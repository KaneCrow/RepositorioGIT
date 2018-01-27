package com.websystique.springmvc.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.websystique.springmvc.model.Results;
import com.websystique.springmvc.model.Routes;
import com.websystique.springmvc.service.FlightsService;

@RestController
public class SpringFlightRestController {

	@Autowired
	FlightsService userService;
	
	@RequestMapping(value = "/interconnections", method = RequestMethod.GET)
	public ResponseEntity<List<Results>> interconnections(@RequestParam("departure")String departure, 
															@RequestParam("departureDateTime") String departureDateTime,
															@RequestParam("arrival") String arrival,
															@RequestParam("arrivalDateTime") String arrivalDateTime) {
		List<Results> result = new ArrayList<Results>();
		
		try{
			List<Routes> routes = userService.findAllRouts();
			if(!routes.isEmpty()){
		//Primero comprobar que la ruta directa existe
				
				if(userService.findOutMyRouteDirect(routes, departure, arrival)){
					result = userService.findMyWayDirect(departure, arrival, departureDateTime, arrivalDateTime);
				}	
		//Rellenamos con las posibles rutas con 1 stop			
				List<Results> resultsIndirect = userService.findMyWayIndirect(routes, departure, arrival, departureDateTime, arrivalDateTime);
				for(Results resultIndirect : resultsIndirect){
					result.add(resultIndirect);
				}
			}
		}catch(Exception e){return new ResponseEntity<List<Results>>(result, HttpStatus.INTERNAL_SERVER_ERROR);}
		return new ResponseEntity<List<Results>>(result, HttpStatus.OK);
		
	}
}
