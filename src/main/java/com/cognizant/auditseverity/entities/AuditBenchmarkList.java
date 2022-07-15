package com.cognizant.auditseverity.entities;

import java.util.ArrayList;
import java.util.List;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class AuditBenchmarkList {
	
	private List<AuditBenchmark> auditBenchmark;
	
	public AuditBenchmarkList() {
		auditBenchmark = new ArrayList<>();
	}
	
}
