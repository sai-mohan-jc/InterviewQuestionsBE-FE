package com.banking.audit.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banking.audit.service.AuditRestClientService;

@RestController
@RequestMapping("/api/audit")
public class AuditRestClientController {

    private final AuditRestClientService auditService;

    public AuditRestClientController(AuditRestClientService auditService) {
        this.auditService = auditService;
    }

    @GetMapping("/send-notification-rest")
    public String sendNotification() {
        return auditService.sendAuditNotification();
    }
}