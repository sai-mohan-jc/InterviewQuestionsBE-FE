package com.banking.account.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class AccountNumberGenerator {

    private static final String PREFIX = "ACC";
    private static final int RANDOM_BOUND = 1_000_000;

    private final SecureRandom random = new SecureRandom();

    public String generate() {

        long timestamp = System.currentTimeMillis();

        int randomNumber = random.nextInt(RANDOM_BOUND);

        return PREFIX + timestamp + String.format("%06d", randomNumber);
    }
}