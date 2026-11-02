package com.banking.screening.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banking.screening.dto.ScreeningRequest;
import com.banking.screening.dto.ScreeningResponse;
import com.banking.screening.service.ScreeningService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/screening")
@RequiredArgsConstructor
public class ScreeningController {

    private final ScreeningService screeningService;

    @PostMapping
    public ScreeningResponse screen(
            @Valid @RequestBody ScreeningRequest request) {

        return screeningService.screen(request);
    }
}