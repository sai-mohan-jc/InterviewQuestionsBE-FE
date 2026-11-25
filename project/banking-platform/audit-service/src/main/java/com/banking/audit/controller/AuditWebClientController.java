package com.banking.audit.controller;

import java.util.concurrent.CompletableFuture;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banking.audit.service.AuditWebClientService;

@RestController
@RequestMapping("/api/audit")
public class AuditWebClientController {

    private final AuditWebClientService auditService;

    public AuditWebClientController(
            AuditWebClientService auditService) {
        this.auditService = auditService;
    }

    @GetMapping("/send-notification")
    public CompletableFuture<String> sendNotification() {

        System.out.println(
            "Request thread: " +
            Thread.currentThread().getName()
        );

        return auditService.sendAuditNotification();
    }
}