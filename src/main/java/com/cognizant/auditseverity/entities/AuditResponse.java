package com.cognizant.auditseverity.entities;

import javax.persistence.*;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AuditResponse {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int auditId;
	private String projectExecutionStatus;
	private String remedialActionDuration;
	@OneToOne(cascade = CascadeType.ALL)
	private AuditRequest auditRequest;
	
}
