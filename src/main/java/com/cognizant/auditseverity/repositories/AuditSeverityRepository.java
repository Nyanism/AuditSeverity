package com.cognizant.auditseverity.repositories;

import com.cognizant.auditseverity.entities.AuditResponse;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditSeverityRepository extends JpaRepository<AuditResponse, Integer>{
	
	AuditResponse findByAuditId(int auditResponseId);
	
}
