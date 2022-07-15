package com.cognizant.auditseverity.services;

import com.cognizant.auditseverity.entities.AuditResponse;
import com.cognizant.auditseverity.repositories.AuditSeverityRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuditSeverityServiceImpl implements AuditSeverityService{
	
	@Autowired
	private AuditSeverityRepository auditSeverityRepository; 
	
	@Override
	public AuditResponse saveAuditResponse(AuditResponse auditResponse) {
		
		return auditSeverityRepository.save(auditResponse);
	}

	@Override
	public AuditResponse findAuditResponse(int auditResponseId) {

		return auditSeverityRepository.findByAuditId(auditResponseId);
	}

}
