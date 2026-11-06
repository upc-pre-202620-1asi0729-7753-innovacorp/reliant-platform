package com.innovacorp.reliant.platform.shared.application.result;

import org.jspecify.annotations.NullMarked;

/**
 * Error produced by the application layer. The code suffix drives the HTTP status
 * (see ProblemDetailFromApplicationErrorAssembler).
 *
 * @param code    machine-readable code (e.g. "HVOF-SYSTEM_NOT_FOUND", "RECIPE_CONFLICT")
 * @param message human-readable message
 * @param details optional extra context
 */
@NullMarked
public record ApplicationError(
        String code,
        String message,
        String details) {

    public ApplicationError(String code, String message) {
        this(code, message, null);
    }

    /** Validation error: input data is invalid or violates constraints → 400. */
    public static ApplicationError validationError(String fieldOrConcept, String reason) {
        return new ApplicationError(
                "VALIDATION_ERROR",
                "Validation failed: %s".formatted(fieldOrConcept),
                reason);
    }

    /** Not found: the requested resource does not exist → 404. */
    public static ApplicationError notFound(String resourceType, String identifier) {
        return new ApplicationError(
                "%s_NOT_FOUND".formatted(resourceType.toUpperCase()),
                "%s not found: %s".formatted(resourceType, identifier),
                identifier);
    }

    /** Business rule violation: operation violates domain constraints → 422. */
    public static ApplicationError businessRuleViolation(String rule, String reason) {
        return new ApplicationError(
                "BUSINESS_RULE_VIOLATION",
                "Business rule violation: %s".formatted(rule),
                reason);
    }

    /** Conflict: operation cannot be completed due to conflicting state → 409. */
    public static ApplicationError conflict(String resource, String reason) {
        return new ApplicationError(
                "%s_CONFLICT".formatted(resource.toUpperCase()),
                "Conflict with %s".formatted(resource),
                reason);
    }

    /** Unexpected error → 500. */
    public static ApplicationError unexpected(String context, String reason) {
        return new ApplicationError(
                "UNEXPECTED_ERROR",
                "Unexpected error in %s".formatted(context),
                reason);
    }
}