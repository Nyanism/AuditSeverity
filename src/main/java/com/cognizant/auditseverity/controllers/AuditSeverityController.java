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
import com.cognizant.auditseverity.services.TokenService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class AuditSeverityController {

	@Autowired
	private AuditSeverityService auditSeverityService;
	@Autowired
	private AuditBenchmarkFeign auditBenchmarkFeign;
	@Autowired
	private TokenService tokenService;
	// private RestTemplate restTemplate;

	/**
	 * Provides a REST endpoint to check if the microservice is running on its
	 * deployed server.
	 * 
	 * @return Returns a ResponseEntity with status code 200 and a small message
	 */
	@GetMapping("/servercheck")
	public ResponseEntity<String> awsCheck() {
		return ResponseEntity.ok("AuditSeverity microservice is running on this server");
	}

	/**
	 * Determines the status of the project based on the audit request and saves
	 * both the request and the response to a database.
	 * 
	 * @param token JWT token
	 * @param auditRequest Details of the audit requested
	 * @return A ResponseEntity with the status code of the request and the response
	 *         if any
	 * @throws URISyntaxException
	 */

	@PostMapping("/ProjectExecutionStatus")
	public ResponseEntity<?> projectExecutionStatus(
			@RequestHeader(name = "Authorization", required = true) String token,
			@RequestBody AuditRequest auditRequest) throws URISyntaxException {

		log.info("Request for project execution status received by AuditSeverity microservice");
		
		if(!tokenService.checkTokenValidity(token)) {
			return new ResponseEntity<String>("Invalid token", HttpStatus.FORBIDDEN);
		}
		
		// Replaced with Feign below
		// To replace string with actual AuditBenchmark URL
		// URI uri = new URI("http://localhost:8080/AuditBenchmark");
		// List<AuditBenchmark> auditBenchmarkList = restTemplate.getForEntity(uri,
		// AuditBenchmarkList.class).getBody().getAuditBenchmark();

		// Obtains the list of AuditBenchmark with a GET request via Feign client
		List<AuditBenchmark> auditBenchmarkList = auditBenchmarkFeign.auditBenchmark().getBody();

		// Extracts the acceptable number of NO responses from AuditBenchmarkList based
		// on the type of audit
		String auditType = auditRequest.getAuditDetail().getAuditType();
		int benchmarkNoAnswers = 0;
		for (AuditBenchmark ab : auditBenchmarkList) {
			if (ab.getAuditType().equalsIgnoreCase(auditType)) {
				benchmarkNoAnswers = ab.getBenchmarkNoAnswers();
			}
		}
		log.info("Number of benchmark no answers retrieved from AuditBenchmark microservice");

		// (for internal testing)
		// String auditType = "SOX";
		// int benchmarkNoAnswers = 2;

		// Determines the number of questions with a NO response from the AuditRequest
		int numberOfAuditQuestionsWithNo = auditRequest.getAuditDetail().getAuditQuestions().stream().filter(q -> {
			return q.getResponse().equalsIgnoreCase("no");
		}).collect(Collectors.toList()).size();
		log.info("Number of no answers retrieved from AuditRequest");

		// (for internal testing)
		// int numberOfAuditQuestionsWithNo = 2;

		// Creates a new AuditResponse and compares the number of NO responses between
		// the benchmark and the request
		AuditResponse auditResponse = new AuditResponse();
		auditResponse.setAuditRequest(auditRequest);
		log.info("Comparing number of no answers from AuditRequest against the number of benchmark no answers");
		if (auditType.equalsIgnoreCase("internal")) {
			if (numberOfAuditQuestionsWithNo <= benchmarkNoAnswers) {
				auditResponse.setProjectExecutionStatus("GREEN");
				auditResponse.setRemedialActionDuration("No action needed");
			} else {
				auditResponse.setProjectExecutionStatus("RED");
				auditResponse.setRemedialActionDuration("Action to be taken in 2 weeks");
			}
		} else {
			if (numberOfAuditQuestionsWithNo <= benchmarkNoAnswers) {
				auditResponse.setProjectExecutionStatus("GREEN");
				auditResponse.setRemedialActionDuration("No action needed");
			} else {
				auditResponse.setProjectExecutionStatus("RED");
				auditResponse.setRemedialActionDuration("Action to be taken in 1 week");
			}
		}

		// Saves the AuditResponse in the database
		auditResponse = auditSeverityService.saveAuditResponse(auditResponse);
		log.info("Saving AuditResponse to database");

		log.info("Request for project execution status completed");
		return new ResponseEntity<AuditResponse>(auditResponse, HttpStatus.OK);
	}

}
