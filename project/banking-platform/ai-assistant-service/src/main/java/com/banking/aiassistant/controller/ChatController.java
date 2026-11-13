package com.banking.aiassistant.controller;

import org.springframework.web.bind.annotation.*;

import com.banking.aiassistant.dto.ChatRequest;
import com.banking.aiassistant.service.ChatService;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;

@RestController
@RequestMapping("/api/ai")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }
    
    @GetMapping("/health")
    public String health() {
        return "AI Assistant Service is running";
    }
    
    
    @PostMapping("/chat")
    public String chat(
            @RequestBody ChatRequest request,
            @AuthenticationPrincipal Jwt jwt) {
    	
    	System.out.println("Get value :"+jwt.getClaimAsString("customerCif"));

        return chatService.askQuestion(
                request.question(),
                jwt.getTokenValue(),
                jwt.getClaimAsString("customerCif")
        );
    }
}