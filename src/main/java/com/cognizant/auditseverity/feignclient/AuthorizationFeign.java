package com.cognizant.auditseverity.feignclient;

import com.cognizant.auditseverity.entities.AuthResponse;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value="AuthorizationFeign", url="{audit.authorization.url}")
public interface AuthorizationFeign {
	
	@GetMapping("/validate")
	ResponseEntity<AuthResponse> validity(@RequestHeader("Authorization") String token);
	
}
