package com.websystique.springmvc;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.web.client.RestTemplate;

public class SpringRestTestClient {

	public static final String REST_SERVICE_URI = "http://localhost:8080/SpringFlight";
	
	/* GET */
	@SuppressWarnings("unchecked")
	private static void listAllUsers(){
		System.out.println("Testing listAllUsers API-----------");
		
		RestTemplate restTemplate = new RestTemplate();
		List<LinkedHashMap<String, Object>> usersMap = restTemplate.getForObject(REST_SERVICE_URI+"/user/", List.class);
		
		if(usersMap!=null){
			for(LinkedHashMap<String, Object> map : usersMap){
	            System.out.println("User : id="+map.get("id")+", Name="+map.get("name")+", Age="+map.get("age")+", Salary="+map.get("salary"));;
	        }
		}else{
			System.out.println("No user exist----------");
		}
	}
	
}