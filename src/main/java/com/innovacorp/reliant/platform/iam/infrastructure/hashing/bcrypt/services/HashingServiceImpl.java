package com.innovacorp.reliant.platform.iam.infrastructure.hashing.bcrypt.services;

import com.innovacorp.reliant.platform.iam.application.internal.outboundservices.hashing.HashingService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.innovacorp.reliant.platform.iam.infrastructure.hashing.bcrypt.BCryptHashingService;

/**
 * Hashes passwords with BCrypt.
 */
@Service
public class HashingServiceImpl implements BCryptHashingService {

    private final BCryptPasswordEncoder passwordEncoder;

    HashingServiceImpl() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public String encode(CharSequence rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}