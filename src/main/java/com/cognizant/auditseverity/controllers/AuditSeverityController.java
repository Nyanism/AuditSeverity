package com.cognizant.auditseverity.controllers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import com.cognizant.auditseverity.entities.AuditBenchmark;
import com.cognizant.auditseverity.entities.AuditBenchmarkList;
import com.cognizant.auditseverity.entities.AuditQuestion;
import com.cognizant.auditseverity.entities.AuditRequest;
import com.cognizant.auditseverity.entities.AuditResponse;
import com.cognizant.auditseverity.feignclient.AuditBenchmarkFeign;
import com.cognizant.auditseverity.services.AuditSeverityService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class AuditSeverityController {

	@Autowired
	private AuditSeverityService auditSeverityService;
	@Autowired
	private AuditBenchmarkFeign auditBenchmarkFeign;
	// private RestTemplate restTemplate;

	@PostMapping("/ProjectExecutionStatus")
	public ResponseEntity<AuditResponse> projectExecutionStatus(@RequestBody AuditRequest auditRequest)
			throws URISyntaxException {
		
		// Replaced with Feign below
		// To replace string with actual AuditBenchmark URL
		// URI uri = new URI("http://localhost:8080/AuditBenchmark");
		// List<AuditBenchmark> auditBenchmarkList = restTemplate.getForEntity(uri, AuditBenchmarkList.class).getBody().getAuditBenchmark();
		
		// Obtains the list of AuditBenchmark with a GET request via Feign client
		List<AuditBenchmark> auditBenchmarkList = auditBenchmarkFeign.auditBenchmark().getBody();
		
		// Extracts the acceptable number of NO responses from AuditBenchmarkList based on the type of audit
		String auditType = auditRequest.getAuditDetail().getAuditType();
		int benchmarkNoAnswers = 0;
		for(AuditBenchmark ab: auditBenchmarkList) {
			if(ab.getAuditType().equalsIgnoreCase(auditType)) {
				benchmarkNoAnswers = ab.getBenchmarkNoAnswers();
			}
		}
		
		// (for internal testing)
		// String auditType = "SOX";
		// int benchmarkNoAnswers = 2;
		
		// Determines the number of questions with a NO response from the AuditRequest
		int numberOfAuditQuestionsWithNo = auditRequest.getAuditDetail().getAuditQuestions().stream().filter(q -> {
			return q.getResponse().equalsIgnoreCase("no"); 
		}).collect(Collectors.toList()).size();
		
		// (for internal testing)
		// int numberOfAuditQuestionsWithNo = 2;
		
		// Creates a new AuditResponse and compares the number of NO responses between the benchmark and the request
		AuditResponse auditResponse = new AuditResponse();
		auditResponse.setAuditRequest(auditRequest);
		if(auditType.equalsIgnoreCase("internal")) {
			if(numberOfAuditQuestionsWithNo <= benchmarkNoAnswers) {
				auditResponse.setProjectExecutionStatus("GREEN");
				auditResponse.setRemedialActionDuration("No action needed");
			} else {
				auditResponse.setProjectExecutionStatus("RED");
				auditResponse.setRemedialActionDuration("Action to be taken in 2 weeks");
			}
		} else {
			if(numberOfAuditQuestionsWithNo <= benchmarkNoAnswers) {
				auditResponse.setProjectExecutionStatus("GREEN");
				auditResponse.setRemedialActionDuration("No action needed");
			} else {
				auditResponse.setProjectExecutionStatus("RED");
				auditResponse.setRemedialActionDuration("Action to be taken in 1 week");
			}
		}
		
		// Saves the AuditResponse in the database
		auditResponse = auditSeverityService.saveAuditResponse(auditResponse);
		
		return new ResponseEntity<AuditResponse>(auditResponse, HttpStatus.OK);
	}

}
