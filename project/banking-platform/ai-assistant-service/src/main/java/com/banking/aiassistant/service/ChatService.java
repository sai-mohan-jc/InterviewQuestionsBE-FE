package com.banking.aiassistant.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final ChatClient chatClient;

    public ChatService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public String askQuestion(String question) {
    	try {
          

            String response = chatClient.prompt()
                    .user(question)
                    .call()
                    .content();

           

            return response;

        } catch (Exception e) {
        	
            throw e;
        }
    }
    
}