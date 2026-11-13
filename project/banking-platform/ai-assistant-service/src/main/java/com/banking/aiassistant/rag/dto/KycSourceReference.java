package com.banking.aiassistant.rag.dto;

public record KycSourceReference(
	    String fileName,
	    Integer pageNumber,
	    String content
	) {}