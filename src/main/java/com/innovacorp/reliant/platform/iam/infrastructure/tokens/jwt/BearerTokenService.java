package com.innovacorp.reliant.platform.iam.infrastructure.tokens.jwt;

import com.innovacorp.reliant.platform.iam.application.internal.outboundservices.tokens.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

/**
 * Bearer token service: TokenService plus HTTP-aware helpers.
 */
public interface BearerTokenService extends TokenService {
    String getBearerTokenFrom(HttpServletRequest request);

    String generateToken(Authentication authentication);
}
