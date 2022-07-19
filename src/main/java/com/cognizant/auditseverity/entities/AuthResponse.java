package com.cognizant.auditseverity.entities;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
	
	private String uid;
	private boolean isValid;
	
}
