package com.banking.aiassistant.controller;

import org.springframework.web.bind.annotation.*;

import com.banking.aiassistant.dto.ChatRequest;
import com.banking.aiassistant.service.ChatService;

@RestController
@RequestMapping("/api/ai")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/chat")
    public String chat(@RequestBody ChatRequest request) {
    

        return chatService.askQuestion(request.question());
    }
    
    @GetMapping("/health")
    public String health() {
        System.out.println("AI Assistant API is reached");
        return "AI Assistant Service is running";
    }
}