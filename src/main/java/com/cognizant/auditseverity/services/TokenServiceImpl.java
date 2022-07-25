package com.cognizant.auditseverity.services;

import com.cognizant.auditseverity.entities.AuthResponse;
import com.cognizant.auditseverity.feignclient.AuthorizationFeign;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService{
	
	@Autowired
	private AuthorizationFeign authorizationFeign;
	
	public boolean checkTokenValidity(String token) {
		
		AuthResponse authReponse = authorizationFeign.validity(token).getBody();
		
		return authReponse.isValid();
	};
	
}
