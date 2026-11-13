package com.banking.aiassistant.rag.dto;

import java.util.List;

public record KycRagResponse(
    String question,
    String answer,
    List<KycSourceReference> sources
) {}