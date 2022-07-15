package com.cognizant.auditseverity.entities;

import java.util.*;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AuditDetail {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String auditType;
	private Date auditDate;
	@ElementCollection
	private List<AuditQuestion> auditQuestions;
	
}
