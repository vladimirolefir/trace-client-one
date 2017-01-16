package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.apache.commons.logging.Log; 
import org.apache.commons.logging.LogFactory; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

@SpringBootApplication
@RestController
public class Devoxx1Application {

	private static final Logger LOG = Logger.getLogger(Devoxx1Application.class.getName());

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private HttpServletRequest request;
	
	public static void main(String[] args) {
		SpringApplication.run(Devoxx1Application.class, args);
	}

	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

	@Bean
	public AlwaysSampler defaultSampler() {
		return new AlwaysSampler();
	}

	@RequestMapping("/")
	public String home() {

		Enumeration headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String key = (String) headerNames.nextElement();
			String value = request.getHeader(key);
			//LOG.log(Level.INFO, "KEY: " + key + " VALUE: " + value);
		}

		//LOG.log(Level.INFO, "you called home");
		return "Hello World from Stage";
	}

	@RequestMapping("/callhome")
	public String callHome() {
		//LOG.log(Level.INFO, "calling home");
		return restTemplate.getForObject("https://trace-client-one.herokuapp.com", String.class);
	}
	
	@RequestMapping("/calltwo")
	public String callTwo() {
		//LOG.log(Level.INFO, "calling two");
		
		Enumeration headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String key = (String) headerNames.nextElement();
			String value = request.getHeader(key);
			if(key.startsWith("X-")){
				//LOG.log(Level.INFO, "KEY: " + key + " VALUE: " + value);
			}
		}		
		
		return restTemplate.getForObject("https://trace-client-two.herokuapp.com/reqtwo", String.class);
	}	
	
	@RequestMapping("/ping")
	public String ping() {

		return "Hello World From Devoxx1Application";
	}

	@RequestMapping("/znext")
	public String zNext() {
		
		return restTemplate.getForObject("https://trace-client-three.herokuapp.com/znext", String.class);
	}
}
