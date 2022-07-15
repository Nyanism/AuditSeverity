package com.cognizant.auditseverity.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AuditRequest {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String projectName;
	private String projectManagerName;
	private String applicationOwnerName;
	@OneToOne(cascade = CascadeType.ALL)
	private AuditDetail auditDetail;
	
}
