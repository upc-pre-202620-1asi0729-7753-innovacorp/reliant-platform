package com.innovacorp.reliant.platform.iam.application.internal.outboundservices.hashing;

/**
 * Hashing service port used by the application layer.
 */
public interface HashingService {
    String encode(CharSequence rawPassword);

    boolean matches(CharSequence rawPassword, String encodedPassword);
}
