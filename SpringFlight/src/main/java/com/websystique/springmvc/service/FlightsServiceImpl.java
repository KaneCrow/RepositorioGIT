package com.websystique.springmvc.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.websystique.springmvc.model.Flight;
import com.websystique.springmvc.model.FlightDay;
import com.websystique.springmvc.model.FlightsMonth;
import com.websystique.springmvc.model.ResultFlightDet;
import com.websystique.springmvc.model.Results;
import com.websystique.springmvc.model.Routes;


@Service("userService")
@Transactional
public class FlightsServiceImpl implements FlightsService{
	
	public List<Routes> findAllRouts() {
		return getAllRouteFromApi();
	}
	
	public Boolean findOutMyRouteDirect(List<Routes> routes, String departure, String arrival){
		
		Routes myRoute = new Routes(departure, arrival, null);
		for(Routes ruta : routes){
			if(myRoute.equals(ruta)){return true;}
			else{ continue;}
		}
		
		return false;
	}
	
	public List<Results> findMyWayDirect(String departure, String arrival, String departureDateTime, String arrivalDateTime){
		
		List<Results> result = new ArrayList<Results>();
//		DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm");
		DateTime jodaTime = DateTime.parse(departureDateTime);
		DateTime jodaTimeArr = DateTime.parse(arrivalDateTime);
		FlightsMonth flights = new FlightsMonth();
		List<ResultFlightDet> list = new ArrayList<ResultFlightDet>();
		Results res = new Results();
		List<ResultFlightDet> singleLeg = new ArrayList<ResultFlightDet>();

		if(jodaTime.isBefore(jodaTimeArr)){
//			int difM = Months.monthsBetween(jodaTime, jodaTimeArr).getMonths();
			int difM = monthsBetween(jodaTime, jodaTimeArr);
			for(int x=0; x <= difM; x++){
				DateTime jodaTimeAux = jodaTime.plusMonths(x);
				flights = new FlightsMonth(); 
				try{
					flights = getAllDaysFlightsFromApi(departure, arrival, jodaTimeAux.getYear(), jodaTimeAux.getMonthOfYear());
//					Gson gson = new Gson();
					list = new ArrayList<ResultFlightDet>(); 
					list = getFlightResultDetail(departure, arrival, departureDateTime, arrivalDateTime, flights, jodaTimeAux.getYear());

					for(int c=0; c < list.size();c++){
						res = new Results();
						res.setStops(0);
						singleLeg = new ArrayList<ResultFlightDet>();
						singleLeg.add(list.get(c));
						res.setLegs(singleLeg);
						result.add(res);

					}
				}catch(Exception e){
					continue;
				}
			}
		}
		return result;
	}

	private static List<Routes> getAllRouteFromApi(){
		ResponseEntity<List<Routes>> rateResponse =
		        new RestTemplate().exchange("https://api.ryanair.com/core/3/routes",
		                    HttpMethod.GET, null, new ParameterizedTypeReference<List<Routes>>() {
		            });
		List<Routes> rates = rateResponse.getBody();
		return rates;
	}
	
	private static FlightsMonth getAllDaysFlightsFromApi(String departure, String arrival, int year, int month){
		ResponseEntity<FlightsMonth> rateResponse =
		        new RestTemplate().exchange("https://api.ryanair.com/timetable/3/schedules/"+ departure +"/"+ arrival +"/years/"+ year +"/months/"+ month,
		                    HttpMethod.GET, null, new ParameterizedTypeReference<FlightsMonth>() {
		            });
		FlightsMonth rates = rateResponse.getBody();
		return rates;
	}
	
	private List<ResultFlightDet> getFlightResultDetail(String departure, String arrival, 
													String departureDateTime, String arrivalDateTime, FlightsMonth flights, int year) {

		List<ResultFlightDet> resultado = new ArrayList<ResultFlightDet>();
		DateTimeFormatter dateTimeFormatter1 = DateTimeFormat.forPattern("yyyy-MM-dd");
		DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm");
		DateTime jodaTimeDeparture = DateTime.parse(departureDateTime);
		DateTime jodaTimeArrival = DateTime.parse(arrivalDateTime);
//		Gson gson = new Gson();
		for(FlightDay flysDay : flights.getDays()){

			if((jodaTimeDeparture.toString(dateTimeFormatter1).equals(DateTime.parse(year+"-"+new Integer(flights.getMonth())+"-"+flysDay.getDay()).toString(dateTimeFormatter1))
					|| jodaTimeDeparture.isBefore(DateTime.parse(year+"-"+new Integer(flights.getMonth())+"-"+flysDay.getDay())))
				&& (jodaTimeArrival.toString(dateTimeFormatter1).equals(DateTime.parse(year+"-"+new Integer(flights.getMonth())+"-"+flysDay.getDay()).toString(dateTimeFormatter1))
					|| jodaTimeArrival.isAfter(DateTime.parse(year+"-"+new Integer(flights.getMonth())+"-"+flysDay.getDay())))){

				for(Flight vuelo : flysDay.getFlights()){
					
					DateTimeFormatter dateTimeFormatterVuelo = DateTimeFormat.forPattern("HH:mm");
					dateTimeFormatterVuelo.parseDateTime(vuelo.getDepartureTime());
					DateTime jodaTimeDepartureVuelo = dateTimeFormatterVuelo.parseDateTime(vuelo.getDepartureTime());
					DateTime jodaTimeArrivalVuelo = dateTimeFormatterVuelo.parseDateTime(vuelo.getArrivalTime());
					
					if((dateTimeFormatter.parseDateTime(year+"-"+new Integer(flights.getMonth())+"-"+flysDay.getDay()+"T"+vuelo.getDepartureTime()).isAfter(jodaTimeDeparture)
							&& dateTimeFormatter.parseDateTime(year+"-"+new Integer(flights.getMonth())+"-"+flysDay.getDay()+"T"+vuelo.getDepartureTime()).isBefore(jodaTimeArrival))
							&& (dateTimeFormatter.parseDateTime(year+"-"+new Integer(flights.getMonth())+"-"+flysDay.getDay()+"T"+vuelo.getArrivalTime()).isBefore(jodaTimeArrival)
							&& dateTimeFormatter.parseDateTime(year+"-"+new Integer(flights.getMonth())+"-"+flysDay.getDay()+"T"+vuelo.getArrivalTime()).isAfter(jodaTimeDeparture))){
						ResultFlightDet result = new ResultFlightDet();
						result.setDepartureAirport(departure);
						result.setArrivalAirport(arrival);
						DateTime dtDeparture = new DateTime(year, new Integer(flights.getMonth()), flysDay.getDay(), jodaTimeDepartureVuelo.getHourOfDay(), jodaTimeDepartureVuelo.getMinuteOfHour(), 0, 0);
						DateTime dtArrival = new DateTime(year, new Integer(flights.getMonth()), flysDay.getDay(), jodaTimeArrivalVuelo.getHourOfDay(), jodaTimeArrivalVuelo.getMinuteOfHour(), 0, 0);
						result.setDepartureDateTime(dtDeparture.toString(dateTimeFormatter));
						result.setArrivalDateTime(dtArrival.toString(dateTimeFormatter));
						resultado.add(result);
					}else{
						continue;
					}
				}
			}else{
				continue;
			}
		}
		return resultado;
	}
	
	public List<Results> findMyWayIndirect (List<Routes> routes, String departure, String arrival, 
													String departureDateTime, String arrivalDateTime){
		
		List<Results> result = new ArrayList<Results>();
		DateTime jodaTime = DateTime.parse(departureDateTime);
		DateTime jodaTimeArr = DateTime.parse(arrivalDateTime);
//		Gson gson = new Gson();
		if(jodaTime.isBefore(jodaTimeArr)){
			Collection<String> perhaps = findOutInDirectFlights(routes, departure, arrival);
			if(!perhaps.isEmpty()){
//				int difM = Months.monthsBetween(jodaTime, jodaTimeArr).getMonths();
				int difM = monthsBetween(jodaTime, jodaTimeArr);
				for(int x=0; x <= difM; x++){
					DateTime jodaTimeAux = jodaTime.plusMonths(x);
					FlightsMonth flights = new FlightsMonth();
					FlightsMonth flights2 = new FlightsMonth();
	
					try{
						for(String posibleInter : perhaps){
							flights = getAllDaysFlightsFromApi(departure, posibleInter, jodaTimeAux.getYear(), jodaTimeAux.getMonthOfYear());
							flights2 = getAllDaysFlightsFromApi(posibleInter, arrival, jodaTimeAux.getYear(), jodaTimeAux.getMonthOfYear());
							
							List<ResultFlightDet> list = new ArrayList<ResultFlightDet>();
							List<ResultFlightDet> list2 = new ArrayList<ResultFlightDet>();
							list = getFlightResultDetail(departure, posibleInter, departureDateTime, arrivalDateTime, flights, jodaTimeAux.getYear());

							for(int c=0; c < list.size();c++){
								DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm");
								DateTime jodaTimeDeparturePlus2 = DateTime.parse(list.get(c).getArrivalDateTime());
								jodaTimeDeparturePlus2.plusHours(2);

								list2 = getFlightResultDetail(posibleInter, arrival, jodaTimeDeparturePlus2.plusHours(2).toString(dateTimeFormatter), arrivalDateTime, flights2, jodaTimeAux.getYear());

								for (int y=0; y<list2.size(); y++){
									Results res = new Results();
									res.setStops(1);
									List<ResultFlightDet> singleLeg = new ArrayList<ResultFlightDet>();
									singleLeg.add(list.get(c));
									singleLeg.add(list2.get(y));
									res.setLegs(singleLeg);
									result.add(res);
								}
							}
						}
					}catch(Exception e){
						continue;
					}
				}
			}
		}
		
		return result;
	}
	
	
	private Collection<String> findOutInDirectFlights(List<Routes> routes, String departure, String arrival){
		
		Routes myRoute = new Routes(departure, arrival, null);
		Collection<String> perhapsArrDep = new ArrayList<String>();
		Collection<String> perhapsDepArr = new ArrayList<String>();
		
		for(Routes ruta : routes){
			if(departure.equals(ruta.getAirportFrom()) && ruta.getConnectingAirport()==null){
				perhapsArrDep.add(ruta.getAirportTo());
			}else if (arrival.equals(ruta.getAirportTo()) && ruta.getConnectingAirport()==null){
				perhapsDepArr.add(ruta.getAirportFrom());
			}else if(myRoute.equals(ruta)){continue;}
			else{continue;}
		}
		
		Collection<String> similar = new HashSet<String>(perhapsArrDep);
		Collection<String> different = new HashSet<String>();
        different.addAll( perhapsArrDep );
        different.addAll( perhapsDepArr );
        similar.retainAll( perhapsDepArr );
        different.removeAll( similar );
		
//        System.out.println("One:%s%nTwo:%s%nSimilar:%s%nDifferent:%s%n", perhapsArrDep, perhapsDepArr, similar, different);
        System.out.println("Similar size::" + similar.size() + "List::" + similar);
		
		return similar;
	}
	
	private Integer monthsBetween (DateTime date1, DateTime date2){
		int result = 0;
		//when the days between dates is not enought to get a month but between dates there is a month change
		if(date1.getYear()==date2.getYear()){
			result = date2.getMonthOfYear() - date1.getMonthOfYear();
		}else{
			result = Months.monthsBetween(date1, date2).getMonths()+1;
		}
		
		return result;
	}
}
