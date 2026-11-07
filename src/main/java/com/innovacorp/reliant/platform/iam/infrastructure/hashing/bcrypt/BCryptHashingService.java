
package com.innovacorp.reliant.platform.iam.infrastructure.hashing.bcrypt;

import com.innovacorp.reliant.platform.iam.application.internal.outboundservices.hashing.HashingService;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Marker interface: one bean serves both the application port and Spring Security's PasswordEncoder.
 */
public interface BCryptHashingService extends HashingService, PasswordEncoder {
}
