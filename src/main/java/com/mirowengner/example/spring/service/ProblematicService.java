package com.mirowengner.example.spring.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class ProblematicService {
	
	private final LockerService service;
	
	@Autowired
	public ProblematicService(LockerService service) {
		this.service = service;
	}
	
	public void process() {
		
	}
}
