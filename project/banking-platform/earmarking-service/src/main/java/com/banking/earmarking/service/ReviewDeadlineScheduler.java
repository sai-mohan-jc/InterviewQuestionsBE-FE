package com.banking.earmarking.service;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.banking.earmarking.entity.ReviewStatus;
import com.banking.earmarking.repository.ReviewCaseRepository;

@Component
public class ReviewDeadlineScheduler {

    private final ReviewCaseRepository repository;

    public ReviewDeadlineScheduler(ReviewCaseRepository repository) {
        this.repository = repository;
    }

    @Scheduled(fixedDelay = 1000000)
    @Transactional
    public void markOverdueCases() {

        var overdueCases =
                repository.findByStatusAndReviewDeadlineBefore(
                        ReviewStatus.PENDING,
                        LocalDateTime.now());

        for (var reviewCase : overdueCases) {
            reviewCase.setStatus(ReviewStatus.OVERDUE);
        }

        if (!overdueCases.isEmpty()) {
            repository.saveAll(overdueCases);
        }
    }
}