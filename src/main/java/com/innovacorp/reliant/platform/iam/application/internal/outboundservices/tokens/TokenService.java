package com.innovacorp.reliant.platform.iam.application.internal.outboundservices.tokens;

/**
 * Token service port used by the application layer.
 */
public interface TokenService {

    String generateToken(String username);

    String getUsernameFromToken(String token);

    boolean validateToken(String token);
}