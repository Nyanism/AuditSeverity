package com.cognizant.auditseverity.services;

import com.cognizant.auditseverity.entities.AuditResponse;

public interface AuditSeverityService {
	
	AuditResponse saveAuditResponse(AuditResponse auditResponse);
	
	AuditResponse findAuditResponse(int auditResponseId);
	
}
