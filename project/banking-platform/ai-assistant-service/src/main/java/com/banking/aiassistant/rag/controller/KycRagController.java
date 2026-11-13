
package com.banking.aiassistant.rag.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banking.aiassistant.rag.dto.KycRagResponse;
import com.banking.aiassistant.rag.service.KycDocumentIngestionService;
import com.banking.aiassistant.rag.service.KycRagService;

@RestController
@RequestMapping("/api/ai/rag")
public class KycRagController {

	private final KycDocumentIngestionService ingestionService;
	private final KycRagService kycRagService;

	public KycRagController(KycDocumentIngestionService ingestionService, KycRagService kycRagService) {
		this.ingestionService = ingestionService;
		this.kycRagService = kycRagService;
	}

	@PostMapping("/ingest/kyc")
	public Map<String, Object> ingestKycDocument() {

		int chunksStored = ingestionService.ingestKycDocument();

		return Map.of("message", "KYC document ingested successfully", "chunksStored", chunksStored);
	}

	@PostMapping("/ask")
	public KycRagResponse askKycQuestion(
	        @RequestBody Map<String, String> request) {

	    return kycRagService.askKycQuestion(
	        request.get("question")
	    );
	}
	
	@GetMapping("/test-auth")
	public String testAuth() {
	    return "Authenticated successfully";
	}
}
