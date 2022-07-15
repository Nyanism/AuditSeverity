package com.cognizant.auditseverity.feignclient;

import java.util.List;

import com.cognizant.auditseverity.entities.AuditBenchmark;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "AuditBenchmarkFeign", url="{audit.benchmark.url}")
public interface AuditBenchmarkFeign {
	
	@GetMapping("/AuditBenchmark")
	ResponseEntity<List<AuditBenchmark>> auditBenchmark();
	
}
